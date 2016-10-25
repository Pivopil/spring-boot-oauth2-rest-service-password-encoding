package io.github.pivopil.share.persistence;

import io.github.pivopil.share.entities.impl.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 24.10.16.
 */
@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findByClientId(String clientId);
}
