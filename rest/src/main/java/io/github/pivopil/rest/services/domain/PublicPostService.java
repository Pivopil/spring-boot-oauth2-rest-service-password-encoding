package io.github.pivopil.rest.services.domain;

import io.github.pivopil.share.entities.impl.domain.PublicPost;
import io.github.pivopil.share.persistence.doamin.PublicPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Created on 19.10.16.
 */
@Service
public class PublicPostService {

    @Autowired
    private PublicPostRepository publicPostRepository;

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public PublicPost getSingle(Long id) {
        return publicPostRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<PublicPost> getAll() {
        return publicPostRepository.findAll();
    }

    public PublicPost add(PublicPost post) {
        return publicPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public PublicPost edit(@Param("post") PublicPost post) {
        return publicPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public void delete(@Param("post") PublicPost post) {
        publicPostRepository.delete(post);
    }

}
