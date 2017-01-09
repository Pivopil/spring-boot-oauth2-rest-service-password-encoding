package io.github.pivopil.share.persistence;

import io.github.pivopil.share.entities.impl.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 08.01.17.
 */

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
