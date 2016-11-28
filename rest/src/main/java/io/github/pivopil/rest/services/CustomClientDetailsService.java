package io.github.pivopil.rest.services;

import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created on 24.10.16.
 */
@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    @Autowired
    public CustomClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client  client = clientRepository.findByClientId(clientId);
        if (client == null) {
            throw new ClientRegistrationException("Could not find client with clientId " + clientId);
        }
        return new CustomClientDetailsService.ClientRepositoryDetails(client);
    }

    private final static class ClientRepositoryDetails extends Client implements ClientDetails {

        private static final long serialVersionUID = 1L;

        private ClientRepositoryDetails(Client client) {
            super(client);
        }

        @Override
        public String getClientId() {
            return super.getClientId();
        }

        @Override
        public Set<String> getResourceIds() {
            return null;
        }

        @Override
        public boolean isSecretRequired() {
            return true;
        }

        @Override
        public String getClientSecret() {
            return super.getClientSecret();
        }

        @Override
        public boolean isScoped() {
            return false;
        }

        @Override
        public Set<String> getScope() {
            return new HashSet<>(Arrays.asList(super.getScopes().split(",")));
        }

        @Override
        public Set<String> getAuthorizedGrantTypes() {
            return new HashSet<>(Arrays.asList("password", "refresh_token"));
        }

        @Override
        public Set<String> getRegisteredRedirectUri() {
            return null;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            String[] roles = super.getRoles().split(",");
            List<GrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
            for (String role :roles) {
                simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(role));
            }
            return simpleGrantedAuthorityList;
        }

        @Override
        public Integer getAccessTokenValiditySeconds() {
            return null;
        }

        @Override
        public Integer getRefreshTokenValiditySeconds() {
            return null;
        }

        @Override
        public boolean isAutoApprove(String scope) {
            return false;
        }

        @Override
        public Map<String, Object> getAdditionalInformation() {
            return null;
        }
    }
}
