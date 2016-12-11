package io.github.pivopil.rest.services;

import io.github.pivopil.rest.services.security.CustomSecurityService;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19.10.16.
 */
@Service
public class ContentService {

    private final ContentRepository contentRepository;

    private final CustomSecurityService customSecurityService;

    @Autowired
    public ContentService(ContentRepository contentRepository, CustomSecurityService customSecurityService) {
        this.contentRepository = contentRepository;
        this.customSecurityService = customSecurityService;
    }

    @PreAuthorize("isAuthenticated() && #id != null")
    @PostAuthorize("returnObject == null || hasPermission(returnObject, 'WRITE')")
    public Content getSingle(Long id) {

        Content one = contentRepository.findOne(id);
        List<String> userACL = new ArrayList<>();
        if (one != null) {
            //userACL = this.customSecurityService.getAclForObject(one);
        }

        return one;
    }

    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<Content> getAll(String title) {
        return title != null ? contentRepository.findAllByTitle(title) : contentRepository.findAll();
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && #post != null")
    public Content add(@Param("post") Content post, User user) {
        post = contentRepository.save(post);
        customSecurityService.addAclPermissions(post, user);
        return post;
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#post, 'WRITE') && #post != null")
    public Content edit(@Param("post") Content post) {
        return contentRepository.save(post);
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#post, 'WRITE') && #post != null")
    public void delete(@Param("post") Content post, User user) {
        contentRepository.delete(post);
        customSecurityService.removeAclPermissions(post, user);
    }

    @Transactional
    public void deleteById(Long id, User user) {
        Content content = getSingle(id);
        delete(content, user);
    }
}
