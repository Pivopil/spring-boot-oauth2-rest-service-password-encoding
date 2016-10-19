package io.github.pivopil.rest.services.domain;

import io.github.pivopil.share.entities.impl.domain.AdminPost;
import io.github.pivopil.share.persistence.doamin.AdminPostRepository;
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
public class AdminPostService {

    @Autowired
    private AdminPostRepository adminPostRepository;

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public AdminPost getSingle(Long id) {
        return adminPostRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<AdminPost> getAll() {
        return adminPostRepository.findAll();
    }

    public AdminPost add(AdminPost post) {
        return adminPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public AdminPost edit(@Param("post") AdminPost post) {
        return adminPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public void delete(@Param("post") AdminPost post) {
        adminPostRepository.delete(post);
    }

}
