package io.github.pivopil.rest.services;

import io.github.pivopil.rest.services.security.CustomSecurityService;
import io.github.pivopil.share.builders.Builders;
import io.github.pivopil.share.builders.impl.UserBuilder;
import io.github.pivopil.share.viewmodels.UserViewModel;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.UserRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final CustomSecurityService customSecurityService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, CustomSecurityService customSecurityService) {
        this.userRepository = userRepository;
        this.customSecurityService = customSecurityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return new UserRepositoryUserDetails(user);
    }
// could be used as
//    @PreAuthorize("@customUserDetailsService.canAccessUser(principal, #id)")
   // public boolean canAccessUser(UserRepositoryUserDetails currentUser, Long id) { return false;  }

    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }


    public UserDetails getSingle(Long id) {
        User user = findUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with id = %s does not exist!", id));
        }
        return new UserRepositoryUserDetails(user);
    }

    @PreAuthorize("isAuthenticated() && #id != null")
    @PostAuthorize("returnObject == null || hasPermission(returnObject, 'READ')")
    private User findUserById(Long id) {
        User one = userRepository.findOne(id);
        String ownerOfObject = customSecurityService.getOwnerOfObject(one);
        List<String> acls = customSecurityService.getMyAclForObject(one);
        return one;
    }

    // todo UserViewModel -> User -> saved User -> updated UserViewModel
    public UserViewModel createNewUser(UserViewModel userViewModel) {

        User newUser = null;
        UserBuilder userBuilder = Builders.of(userViewModel, User.class);

        newUser = userBuilder.build();
        newUser = add(newUser);
        if (newUser == null) {
            throw new BadCredentialsException("New user has bad credentials");
        }

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
        return newUser;
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
