package io.github.pivopil.share.persistence.ws;

/**
 * Created on 08.11.16.
 */

import io.github.pivopil.share.entities.impl.ws.SessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SpringSessionRepository extends CrudRepository<SessionEntity, String> {
}
