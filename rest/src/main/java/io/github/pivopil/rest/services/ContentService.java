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

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public Content getSingle(Long id) {
        return contentRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<Content> getAll() {
        return contentRepository.findAll();
    }

    @Transactional
    public Content add(Content post, User user) {
        post = contentRepository.save(post);
        customSecurityService.addAclPermissions(post, user);
        return post;
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public Content edit(@Param("post") Content post) {
        return contentRepository.save(post);
    }

    @Transactional
    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public void delete(@Param("post") Content post, User user) {
        contentRepository.delete(post);
        customSecurityService.removeAclPermissions(post, user);
    }

}
