package io.github.pivopil.share.persistence;

import io.github.pivopil.share.entities.impl.Content;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 19.10.16.
 */
@Repository
public interface ContentRepository extends CrudRepository<Content, Long> {
}
