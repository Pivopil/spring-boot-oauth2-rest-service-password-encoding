package io.github.pivopil.rest.services;

import io.github.pivopil.share.builders.Builders;
import io.github.pivopil.share.builders.impl.ClientBuilder;
import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.exceptions.ExceptionAdapter;
import io.github.pivopil.share.persistence.ClientRepository;
import io.github.pivopil.share.viewmodels.impl.ClientViewModel;
import net.sf.oval.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created on 24.10.16.
 */
@Service
@DependsOn("ovalValidator")
public class CustomClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final Validator ovalValidator;

    @Autowired
    public CustomClientDetailsService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, Validator ovalValidator) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.ovalValidator = ovalValidator;
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clientRepository.findByClientId(clientId);
        if (client == null) {
            throw new ExceptionAdapter(new ClientRegistrationException("Could not find client with clientId " + clientId));
        }
        return new CustomClientDetailsService.ClientRepositoryDetails(client);
    }

    public Iterable<Client> list() {
        return clientRepository.findAll();
    }

    public Client getSingle(Long id) {
        return clientRepository.findOne(id);
    }

    @Transactional
    public ClientViewModel save(Client client) {

        String secret = client.getClientSecret();

        validateSecret(secret);

        ClientBuilder clientBuilder = Builders.of(client);

        String clientSecretEncoded = passwordEncoder.encode(client.getClientSecret());

        client = clientBuilder.clientSecret(clientSecretEncoded).withOvalValidator(ovalValidator).build();

        client = clientRepository.save(client);

        clientBuilder = Builders.of(client);

        return clientBuilder.buildViewModel();
    }


    @Transactional
    public void update(Client client) {

        Long clientId = client.getId();

        Client clientFromDB = clientRepository.findOne(clientId);

        if (clientFromDB == null) {
            throw new ExceptionAdapter(new ClientRegistrationException("Could not find client with clientId " + clientId));
        }

        String secret = client.getClientSecret();

        validateSecret(secret);

        ClientBuilder clientBuilder = Builders.of(client);

        String encodedSecret = passwordEncoder.encode(secret);

        client = clientBuilder.clientSecret(encodedSecret).withOvalValidator(ovalValidator).build();

        clientRepository.save(client);
    }

    private void validateSecret(String secret) {
        if (secret == null || secret.equals("")) {
            throw new ExceptionAdapter(new ClientRegistrationException("Invalid secret for client"));
        }
    }

    @Transactional
    public void delete(Long id) {
        clientRepository.delete(id);
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
            for (String role : roles) {
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
