package io.github.pivopil.share.persistence;

import io.github.pivopil.share.entities.impl.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 06.10.15.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
// AdminPostRepository  PersonalPostRepository