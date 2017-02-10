package io.github.pivopil.rest.services;

import io.github.pivopil.rest.services.security.CustomSecurityService;
import io.github.pivopil.share.builders.Builders;
import io.github.pivopil.share.builders.impl.ContentBuilder;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.persistence.ContentRepository;
import io.github.pivopil.share.viewmodels.impl.ContentViewModel;
import net.sf.oval.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created on 19.10.16.
 */
@Service
@DependsOn("ovalValidator")
public class ContentService {

    private final ContentRepository contentRepository;

    private final CustomSecurityService customSecurityService;

    private final Validator ovalValidator;

    @Autowired
    public ContentService(ContentRepository contentRepository, CustomSecurityService customSecurityService, Validator ovalValidator) {
        this.contentRepository = contentRepository;
        this.customSecurityService = customSecurityService;
        this.ovalValidator = ovalValidator;
    }

    @PreAuthorize("isAuthenticated() && #id != null")
    @PostAuthorize("returnObject == null || hasPermission(returnObject, 'READ')")
    public Content getSingle(Long id) {
        return contentRepository.findOne(id);
    }

    @PreAuthorize("isAuthenticated() && #id != null")
    public ContentViewModel getContentById(Long id) {

        Content content = getSingle(id);

        ContentBuilder contentBuilder = Builders.of(content);

        ContentViewModel contentViewModel = contentBuilder.buildViewModel();

        String ownerOfObject = customSecurityService.getOwnerOfObject(content);
        List<String> acls = customSecurityService.getMyAclForObject(content);

        contentViewModel.setOwner(ownerOfObject);
        contentViewModel.setAcls(acls);

        return contentViewModel;
    }


    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<Content> getAll(String title) {
        return title != null ? contentRepository.findAllByTitle(title) : contentRepository.findAll();
    }


    @PreAuthorize("isAuthenticated() && #content != null")
    public Content add(@Param("content") Content content) {
        content = contentRepository.save(content);
        customSecurityService.addAclPermissions(content);
        return content;
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#content, 'WRITE') && #content != null")
    public Content edit(@Param("content") Content content) {

        ContentBuilder contentBuilder = Builders.of(content);

        content = contentBuilder.withOvalValidator(ovalValidator).build();

        return contentRepository.save(content);
    }

    @PreAuthorize("isAuthenticated() && hasPermission(#content, 'WRITE') && #content != null")
    private void delete(@Param("content") Content content) {
        contentRepository.delete(content);
        customSecurityService.removeAclPermissions(content);
    }

    @Transactional
    public void deleteById(Long id) {
        Content content = getSingle(id);
        delete(content);
    }

    @Transactional
    public ContentViewModel addContent(Content content) {

        ContentBuilder contentBuilder = Builders.of(content);

        content = contentBuilder.withOvalValidator(ovalValidator).build();

        content = add(content);

        contentBuilder = Builders.of(content);

        ContentViewModel contentViewModel = contentBuilder.buildViewModel();

        String ownerOfObject = customSecurityService.getOwnerOfObject(content);
        List<String> acls = customSecurityService.getMyAclForObject(content);

        contentViewModel.setOwner(ownerOfObject);
        contentViewModel.setAcls(acls);

        return contentViewModel;
    }
}
