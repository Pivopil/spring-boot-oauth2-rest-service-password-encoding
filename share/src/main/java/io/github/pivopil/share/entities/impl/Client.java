package io.github.pivopil.share.entities.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.pivopil.share.entities.BasicEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created on 24.10.16.
 */

@Entity
@Table(name = "test_oauth_client_details")
public class Client extends BasicEntity {

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String clientId;

    @JsonIgnore
    @NotEmpty
    private String clientSecret;

    private String scopes;

    public Client() {
    }

    public Client(Client client) {
        super();
        this.setId(client.getId());
        this.clientId = client.clientId;
        this.clientSecret = client.clientSecret;
        this.scopes = client.scopes;
    }

    public Client(String clientId, String clientSecret, String scopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
}
