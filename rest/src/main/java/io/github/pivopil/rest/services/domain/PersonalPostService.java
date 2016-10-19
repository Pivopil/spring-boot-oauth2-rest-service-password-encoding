package io.github.pivopil.rest.services.domain;

import io.github.pivopil.share.entities.impl.domain.PersonalPost;
import io.github.pivopil.share.persistence.doamin.PersonalPostRepository;
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
public class PersonalPostService {

    @Autowired
    private PersonalPostRepository personalPostRepository;


    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public PersonalPost getSingle(Long id) {
        return personalPostRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public Iterable<PersonalPost> getAll() {
        return personalPostRepository.findAll();
    }

    public PersonalPost add(PersonalPost post) {
        return personalPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public PersonalPost edit(@Param("post") PersonalPost post) {
        return personalPostRepository.save(post);
    }

    @PreAuthorize("hasPermission(#post, 'WRITE')")
    public void delete(@Param("post") PersonalPost post) {
        personalPostRepository.delete(post);
    }

}
