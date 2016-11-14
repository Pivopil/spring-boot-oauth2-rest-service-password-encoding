package io.github.pivopil.rest.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActiveWebSocketUserRepository
        extends CrudRepository<ActiveWebSocketUser, String> {

    @Query("select DISTINCT(u.username) from ActiveWebSocketUser u where u.username != :username")
    List<String> findAllActiveUsers(@Param("username") String username);
}
