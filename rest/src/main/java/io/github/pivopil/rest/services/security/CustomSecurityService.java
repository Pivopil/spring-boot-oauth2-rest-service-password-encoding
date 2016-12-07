package io.github.pivopil.rest.services.security;

import io.github.pivopil.rest.constants.ROLES;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 30.11.16.
 */
@Service
public class CustomSecurityService {


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

    public <T> void addAclPermissions(T objectWithId, User user) {
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
                customACLService.persistReadWritePermissionsForDomainObject(objectWithId, user.getLogin(), true);
            }
        }
    }

    public <T> void removeAclPermissions(T objectWithId, User user) {
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
                customACLService.deleteReadWritePermissionsFromDatabase(objectWithId, user.getLogin(), true);
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
}
