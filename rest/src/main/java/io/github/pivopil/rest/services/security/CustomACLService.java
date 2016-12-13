package io.github.pivopil.rest.services.security;

import io.github.pivopil.rest.constants.ROLES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 23.11.16.
 */

@Service
public class CustomACLService {

    private static final Logger log = LoggerFactory.getLogger(CustomACLService.class);


    private final MutableAclService mutableAclService;

    private final ObjectIdentityRetrievalStrategyImpl objectIdentityRetrievalStrategy;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    private final SidRetrievalStrategy sidRetrievalStrategy;

    @Autowired
    public CustomACLService(SidRetrievalStrategy sidRetrievalStrategy,
                            ObjectIdentityRetrievalStrategyImpl objectIdentityRetrievalStrategy,
                            MutableAclService mutableAclService,
                            JdbcMutableAclService jdbcMutableAclService) {
        this.sidRetrievalStrategy = sidRetrievalStrategy;
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
        this.mutableAclService = mutableAclService;
        this.aclPermissionEvaluator = new AclPermissionEvaluator(jdbcMutableAclService);
    }


    private MutableAcl addPermission(ObjectIdentity objectIdentity,
                                     Object userData,
                                     boolean isPrincipal,
                                     Permission permission) {
        MutableAcl mutableAcl;
        try {

            log.debug("Try to find mutableAcl object for '{}'", objectIdentity);
            Acl acl = mutableAclService.readAclById(objectIdentity);
            mutableAcl = MutableAcl.class.cast(acl);
            log.debug("Successfully found mutableAcl object for '{}'", objectIdentity);

        } catch (NotFoundException e) {

            log.debug("Existing ACL is not found and new ACL will be created");
            mutableAcl = mutableAclService.createAcl(objectIdentity);
            log.debug("Successfully created mutableAcl object for '{}'", objectIdentity);

        }

        List<AccessControlEntry> entries = mutableAcl.getEntries();

        log.debug("Try to obtain sid by user data '{}'", userData);
        Sid sid = obtainSidObject(userData, isPrincipal);
        log.debug("Successfully got sid by user data '{}'", userData);

        log.debug("Try to update mutableAcl permission '{}' for object '{}' for user '{}'", permission, objectIdentity, userData);
        mutableAcl.insertAce(entries.size(), permission, sid, true);
        mutableAcl = mutableAclService.updateAcl(mutableAcl);
        log.debug("Successfully updated mutableAcl");

        return mutableAcl;
    }

    private Sid obtainSidObject(Object userData, boolean isPrincipal) {

        log.debug("Try to get principal object from data '{}'", userData);

        if (userData instanceof Sid) {
            log.debug("Successfully got Sid object from data '{}'", userData);
            return Sid.class.cast(userData);
        }
        if (userData instanceof Authentication) {
            log.debug("Successfully got Authentication object from data '{}'", userData);
            return new PrincipalSid(Authentication.class.cast(userData));
        }
        if (userData instanceof String) {

            String stringRecipient = String.class.cast(userData);

            boolean startsWithRolePrefix = stringRecipient.startsWith(ROLES.ROLE_PREFIX);

            if (!isPrincipal && !startsWithRolePrefix) {
                stringRecipient = ROLES.ROLE_PREFIX + userData;
            }

            if (startsWithRolePrefix) {
                log.debug("Successfully got GrantedAuthoritySid object from data '{}'", stringRecipient);
                return new GrantedAuthoritySid(stringRecipient);
            } else {
                log.debug("Successfully got PrincipalSid object from data '{}'", stringRecipient);
                return new PrincipalSid(stringRecipient);
            }
        }
        log.error("No way to get principal object from data '{}'", userData);
        throw new IllegalArgumentException("Illegal principal object");
    }

    void persistAllACLPermissionsForDomainObject(Object objectWithId,
                                                 String userNameOrRoleName,
                                                 boolean isPrincipal) {

        persistGeneralPermissions(objectWithId,
                userNameOrRoleName,
                isPrincipal,
                BasePermission.ADMINISTRATION,
                BasePermission.CREATE,
                BasePermission.DELETE,
                BasePermission.READ,
                BasePermission.WRITE);
    }

    void persistReadWritePermissionsForDomainObject(Object objectWithId,
                                                    String userNameOrRoleName,
                                                    boolean isPrincipal) {

        persistGeneralPermissions(objectWithId,
                userNameOrRoleName,
                isPrincipal,
                BasePermission.READ,
                BasePermission.WRITE);
    }

    private void persistGeneralPermissions(final Object objectWithId,
                                           final String userNameOrRoleName,
                                           boolean isPrincipal,
                                           Permission... selectedPermissions) {

        for (Permission selectedPermission : selectedPermissions) {
            addPermission(objectWithId, userNameOrRoleName, isPrincipal, selectedPermission);
        }

    }

    private MutableAcl addPermission(Object objectWithId, Object userData, boolean isPrincipal, Permission permission) {

        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(objectWithId);

        return addPermission(objectIdentity, userData, isPrincipal, permission);
    }

    private void removeSelectedPermission(ObjectIdentity objectIdentity,
                                          Object userData,
                                          boolean isPrincipal,
                                          final Permission selectedPermission) {
        boolean loopBreak = false;
        try {
            log.debug("Try to get sid by user data '{}'", userData);
            final Sid sid = obtainSidObject(userData, isPrincipal);
            log.debug("Successfully got sid '{}' by user data '{}'", sid, userData);

            log.debug("Try to get acl by objectIdentity '{}'", objectIdentity);
            final Acl acl = mutableAclService.readAclById(objectIdentity);
            log.debug("Successfully got acl '{}' by objectIdentity '{}'", acl, userData);

            final MutableAcl mutableAcl = MutableAcl.class.cast(acl);
            while (!loopBreak) {
                loopBreak = isLoopBreak(selectedPermission, sid, mutableAcl);
            }
            log.debug("Try to persist mutableAcl");
            mutableAclService.updateAcl(mutableAcl);
            log.debug("Successfully persisted mutableAcl");
        } catch (NotFoundException e) {
            log.error("Error while removing selected permissions with message '{}'", e.getMessage());
        }
    }

    private boolean isLoopBreak(Permission selectedPermission, Sid sid, MutableAcl mutableAcl) {
        boolean loopBreak = true;
        final List<AccessControlEntry> mutableAclEntries = mutableAcl.getEntries();
        log.debug("In loop, got {} Access Control Entries", mutableAclEntries.size());

        log.debug("Iterate for Access Control Entries");
        for (int i = 0; i < mutableAclEntries.size(); i++) {

            final AccessControlEntry entry = mutableAclEntries.get(i);
            final Sid entrySid = entry.getSid();
            final Permission permission = entry.getPermission();

            if (entrySid.equals(sid) && permission.equals(selectedPermission)) {
                log.debug("Try to delete Access Control Entry by index '{}'", i);
                mutableAcl.deleteAce(i);
                log.debug("Successfully deleted Access Control Entry by index '{}'", i);

                log.debug("Set loopBreak to false and break inner 'for' loop");
                loopBreak = false;
                break;
            }
        }
        return loopBreak;
    }

    private void removePermission(Object objectWithId,
                                  Object userData,
                                  boolean isPrincipal,
                                  Permission permission) {

        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(objectWithId);

        removeSelectedPermission(objectIdentity, userData, isPrincipal, permission);
    }

    void removeACLByObject(Object objectWithId) {

        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(objectWithId);

        mutableAclService.deleteAcl(objectIdentity, false);
    }

    public MutableAcl changeOwnerForObjectByName(Object objectWithId, String newUsername) {
        MutableAcl acl = retrieveAclForObject(objectWithId);
        PrincipalSid principalSid = new PrincipalSid(newUsername);
        acl.setOwner(principalSid);
        return mutableAclService.updateAcl(acl);
    }

    MutableAcl retrieveAclForObject(Object objectWithId) {
        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(objectWithId);
        Acl acl = mutableAclService.readAclById(objectIdentity);
        return MutableAcl.class.cast(acl);
    }

    List<Sid> retrieveSidsBy(Authentication authentication) {
        return sidRetrievalStrategy.getSids(authentication);
    }

    void removePermissions(final Object objectWithId,
                           final String userNameOrRoleName,
                           boolean isPrincipal,
                           Permission... selectedPermissions) {

        for (Permission selectedPermission : selectedPermissions) {
            removePermission(objectWithId, userNameOrRoleName, isPrincipal, selectedPermission);
        }
    }

    void deleteReadWritePermissionsFromDatabase(Object objectWithId,
                                                String userNameOrRoleName,
                                                boolean isPrincipal) {
        removePermissions(objectWithId,
                userNameOrRoleName,
                isPrincipal,
                BasePermission.READ,
                BasePermission.WRITE);
    }
}
