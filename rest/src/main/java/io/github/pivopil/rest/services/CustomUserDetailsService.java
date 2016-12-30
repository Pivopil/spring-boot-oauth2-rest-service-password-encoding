package io.github.pivopil.rest.services;

import io.github.pivopil.rest.constants.ROLES;
import io.github.pivopil.rest.services.security.CustomACLService;
import io.github.pivopil.rest.services.security.CustomSecurityService;
import io.github.pivopil.share.builders.Builders;
import io.github.pivopil.share.builders.impl.UserBuilder;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.RoleRepository;
import io.github.pivopil.share.persistence.UserRepository;
import io.github.pivopil.share.viewmodels.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CustomSecurityService customSecurityService;

    private final CustomACLService customACLService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, CustomSecurityService customSecurityService, RoleRepository roleRepository, CustomACLService customACLService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customSecurityService = customSecurityService;
        this.roleRepository = roleRepository;
        this.customACLService = customACLService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return new UserRepositoryUserDetails(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }


    public UserViewModel getSingle(Long id) {
        UserViewModel user = findUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with id = %s does not exist!", id));
        }
        return user;
    }

    @PreAuthorize("isAuthenticated() && #id != null")
    @PostAuthorize("returnObject == null || hasPermission(returnObject, 'READ')")
    private UserViewModel findUserById(Long id) {
        User one = userRepository.findOne(id);
        UserBuilder userBuilder = Builders.of(one);
        UserViewModel userViewModel = userBuilder.buildViewModel();
        String ownerOfObject = customSecurityService.getOwnerOfObject(one);
        List<String> acls = customSecurityService.getMyAclForObject(one);
        userViewModel.setOwner(ownerOfObject);
        userViewModel.setAcls(acls);
        return userViewModel;
    }

    public UserViewModel createNewUser(User newUser) {
        Set<Role> roles = new HashSet<>();

        for (Role role : newUser.getRoles()) {
            String name = role.getName();
            Role oneByName = roleRepository.findOneByName(name);
            if (oneByName == null) {
                throw new IllegalArgumentException(String.format("Role with name %s does note exist", name));
            }
            roles.add(oneByName);
        }

        newUser.setRoles(roles);

        if (roles.size() == 0) {
            throw new BadCredentialsException("New User object should have at least one valid role!");
        }

        //case 1 admin can create local_admin and local_user
        Boolean isCreatorAdmin = customSecurityService.isUserHasRole(ROLES.ROLE_ADMIN);

        Boolean hasAdminRole = customSecurityService.isRolesContainRoleName(roles, ROLES.ROLE_ADMIN);

        if (hasAdminRole) {
            throw new BadCredentialsException("User can not create new user with ROLE_ADMIN!");
        }

        if (!isCreatorAdmin) {
            // if user does not have ROLE_ADMIN check if he is LOCAL_ADMIN
            List<Role> localAdminSet = customSecurityService.getRolesNameContains(ROLES.LOCAL_ADMIN);

            if (localAdminSet.size() == 0) {
                throw new BadCredentialsException("User with LOCAL_ADMIN role can not create new user with LOCAL_ADMIN role!");
            }

            String authenticatedUserRoleName = localAdminSet.get(0).getName();

            Boolean isLocalUser = customSecurityService.isRolesContainRoleName(roles, authenticatedUserRoleName.replace(ROLES.LOCAL_ADMIN, ROLES.LOCAL_USER));

            if (isLocalUser) {
                throw new BadCredentialsException("User with LOCAL_ADMIN role can create user only for the same org");
            }
        }

        UserBuilder userBuilder = Builders.of(newUser);
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser = userBuilder.password(encodedPassword).enabled(Boolean.TRUE).build();
        newUser = add(newUser);

        userBuilder = Builders.of(newUser);
        UserViewModel userViewModel = userBuilder.buildViewModel();

        String ownerOfObject = customSecurityService.getOwnerOfObject(newUser);
        List<String> acls = customSecurityService.getMyAclForObject(newUser);

        userViewModel.setOwner(ownerOfObject);
        userViewModel.setAcls(acls);

        return userViewModel;
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && #newUser != null")
    private User add(@Param("newUser") User newUser) {
        newUser = userRepository.save(newUser);
        customSecurityService.addAclPermissions(newUser);
        // let new user edit himself
        customACLService.persistReadWritePermissionsForDomainObject(newUser, newUser.getLogin(), true);
        return newUser;
    }

    @PreAuthorize("isAuthenticated()")
    public UserDetails me() {
        String userLogin = customSecurityService.userLoginFromAuthentication();
        return loadUserByUsername(userLogin);
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#user, 'WRITE') && #user != null")
    public User edit(@Param("user") User user) {
        // todo: implement validation for user object
        return userRepository.save(user);
    }

    // todo: find way to remove user object in case if user is owner if another objects
    @PreAuthorize("isAuthenticated() && hasPermission(#user, 'WRITE') && #user != null")
    private void delete(@Param("user") User user) {
        userRepository.delete(user);
        customSecurityService.removeAclPermissions(user);
        customACLService.deleteReadWritePermissionsFromDatabase(user, user.getLogin(), true);
    }

    @Transactional
    public void deleteById(Long id) {
        User one = userRepository.findOne(id);
        disableUser(one);
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#user, 'WRITE') && #user != null")
    private void disableUser(User user) {
        Boolean hasAdminRole = customSecurityService.isRolesContainRoleName(user.getRoles(), ROLES.ROLE_ADMIN);

        if (hasAdminRole) {
            throw new BadCredentialsException("Admin can not disable himself!");
        }

        user.setEnabled(Boolean.FALSE);
        userRepository.save(user);
    }

    private final static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;

        private UserRepositoryUserDetails(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return getLogin();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return getEnabled();
        }

    }

}
