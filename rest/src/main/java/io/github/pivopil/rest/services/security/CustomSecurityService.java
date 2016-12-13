package io.github.pivopil.rest.services.security;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.pivopil.rest.constants.ROLES;
import io.github.pivopil.share.entities.impl.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 30.11.16.
 */
@Service
public class CustomSecurityService {

    private static final Logger log = LoggerFactory.getLogger(CustomACLService.class);

    private final MutableAclService mutableAclService;

    private final CustomACLService customACLService;

    private final ObjectIdentityRetrievalStrategyImpl identityRetrievalStrategy;

    @Autowired
    public CustomSecurityService(MutableAclService mutableAclService, CustomACLService customACLService, ObjectIdentityRetrievalStrategyImpl identityRetrievalStrategy) {
        this.mutableAclService = mutableAclService;
        this.customACLService = customACLService;
        this.identityRetrievalStrategy = identityRetrievalStrategy;
    }

    private Authentication getAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    private <T> Collection<T> getAuthorities(Class<T> clazz) {
        final Authentication authentication = getAuthentication();
        if (authentication == null) {
            return new ArrayList<>();
        }
        @SuppressWarnings("uncheked")
        Collection<T> authorities = (Collection<T>) getAuthentication().getAuthorities();

        return authorities;
    }

    public <T> void addAclPermissions(T objectWithId) {
        Collection<Role> authorities = getAuthorities(Role.class);
        mutableAclService.createAcl(identityRetrievalStrategy.getObjectIdentity(objectWithId));

        Boolean isRoleAdmin = authorities.stream().filter(i -> i.getName().equals(ROLES.ROLE_ADMIN)).count() > 0;
        // all posts are visible for ROLE_ADMIN
        customACLService.persistAllACLPermissionsForDomainObject(objectWithId, ROLES.ROLE_ADMIN, false);
        if (!isRoleAdmin) {
            // if user does not have ROLE_ADMIN check if he is LOCAL_ADMIN
            List<Role> localAdminSet = authorities.stream().filter(i -> i.getName().contains(ROLES.LOCAL_ADMIN)).collect(Collectors.toList());
            if (localAdminSet.size() > 0) {
                String roleName = localAdminSet.get(0).getName();
                customACLService.persistAllACLPermissionsForDomainObject(objectWithId, roleName, false);
            } else {
                List<Role> localUserSet = authorities.stream().filter(i -> i.getName().contains(ROLES.LOCAL_USER)).collect(Collectors.toList());
                String roleName = localUserSet.get(0).getName();
                String replace = roleName.replace(ROLES.LOCAL_USER, ROLES.LOCAL_ADMIN);
                customACLService.persistAllACLPermissionsForDomainObject(objectWithId, replace, false);
                // if user is not ROLE_ADMIN and not LOCAL_ADMIN
                String userLogin = userLoginFromAuthentication();
                if (userLogin != null) {
                    customACLService.persistReadWritePermissionsForDomainObject(objectWithId, userLogin, true);
                }
            }
        }
    }

    public <T> void removeAclPermissions(T objectWithId) {
        customACLService.removePermissions(objectWithId, ROLES.ROLE_ADMIN, false,
                BasePermission.ADMINISTRATION,
                BasePermission.CREATE,
                BasePermission.DELETE,
                BasePermission.READ,
                BasePermission.WRITE);
        Collection<Role> authorities = getAuthorities(Role.class);
        Boolean isRoleAdmin = authorities.stream().filter(i -> i.getName().equals(ROLES.ROLE_ADMIN)).count() > 0;

        if (!isRoleAdmin) {
            List<Role> localAdminSet = authorities.stream().filter(i -> i.getName().contains(ROLES.LOCAL_ADMIN)).collect(Collectors.toList());
            String roleName;
            if (localAdminSet.size() > 0) {
                roleName = localAdminSet.get(0).getName();
            } else {
                List<Role> localUserSet = authorities.stream().filter(i -> i.getName().contains(ROLES.LOCAL_USER)).collect(Collectors.toList());
                roleName = localUserSet.get(0).getName().replace(ROLES.LOCAL_USER, ROLES.LOCAL_ADMIN);
                String userLogin = userLoginFromAuthentication();
                if (userLogin != null) {
                    customACLService.deleteReadWritePermissionsFromDatabase(objectWithId, userLogin, true);
                }

            }
            customACLService.removePermissions(objectWithId, roleName, false,
                    BasePermission.ADMINISTRATION,
                    BasePermission.CREATE,
                    BasePermission.DELETE,
                    BasePermission.READ,
                    BasePermission.WRITE);
        }
        customACLService.removeACLByObject(objectWithId);
    }

    public String getOwnerOfObject(Object objectWithId) {
        final MutableAcl acl = customACLService.retrieveAclForObject(objectWithId);
        PrincipalSid principalSid = PrincipalSid.class.cast(acl.getOwner());
        return principalSid.getPrincipal();
    }

    private String userLoginFromAuthentication() {
        Object principal = getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }
        UserDetails userDetails = UserDetails.class.cast(principal);
        return userDetails.getUsername();
    }

    public List<String> getACL(Object domain) {
        final List<Sid> sids = customACLService.retrieveSidsBy(getAuthentication());

        final MutableAcl acl = customACLService.retrieveAclForObject(domain);

        ArrayList<Permission> allPermissions = Lists.newArrayList(BasePermission.READ,
                BasePermission.WRITE,
                BasePermission.CREATE,
                BasePermission.DELETE,
                BasePermission.ADMINISTRATION);

        List<String> permissionLabels = new ArrayList<>();

        Map<Integer, String> maskToLabel = ImmutableMap.of(1, "READ", 2, "WRITE", 4, "CREATE", 8, "DELETE", 16, "ADMINISTRATION");

        for (Permission permission : allPermissions) {
            try {
                boolean isGranted = acl.isGranted(Lists.newArrayList(permission), sids, false);
                if (isGranted) {
                    String label = maskToLabel.get(permission.getMask());
                    permissionLabels.add(label);
                }
            } catch (NotFoundException e) {
                log.debug("Get Acl error '{}'", e.getMessage());
            }
        }

        return permissionLabels;
    }
}
