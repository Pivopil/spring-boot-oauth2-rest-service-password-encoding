package io.github.pivopil.rest.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * Created on 30.11.16.
 */
@Service
public class CustomSecurityService {

    private Authentication getAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    public <T> Collection<T> getAuthorities(Class<T> clazz) {
        final Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        @SuppressWarnings("uncheked")
        Collection<T> authorities = (Collection<T>) getAuthentication().getAuthorities();

        return authorities;
    }
}
