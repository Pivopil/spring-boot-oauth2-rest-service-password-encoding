package io.github.pivopil.rest.config;

import io.github.pivopil.REST_API;
import io.github.pivopil.rest.handlers.CustomHttpSessionStrategy;
import io.github.pivopil.rest.services.CustomUserDetailsService;
import io.github.pivopil.share.persistence.ws.JPASessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created on 27.05.16.
 */

@Configuration
@EnableWebSecurity
public class OAuth2Configuration extends WebSecurityConfigurerAdapter {

    private static final String RESOURCE_ID = "restservice";


    @Autowired
    public SessionRepository<ExpiringSession> sessionRepository;

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }


    @Bean
    public SessionRepositoryFilter<ExpiringSession> sessionRepositoryFilter() {
        SessionRepositoryFilter<ExpiringSession> sessionRepositoryFilter = new SessionRepositoryFilter<>(sessionRepository);
        sessionRepositoryFilter.setHttpSessionStrategy(httpSessionStrategy());
        return sessionRepositoryFilter;
    }

    @Bean
    public SessionRepository<ExpiringSession> sessionRepository() {
        return new JPASessionRepository(10);
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextFilter.class)
    public RequestContextFilter requestContextFilter() {

        return new RequestContextFilter();
    }

    @Bean
    public FilterRegistrationBean requestContextFilterChainRegistration(
            @Qualifier("requestContextFilter") Filter securityFilter) {

        FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
        registration.setName("requestContextFilter");

        // note : must previous order of oAuth2ClientContextFilter
        registration.setOrder(SessionRepositoryFilter.DEFAULT_ORDER + 1);

        return registration;
    }

    @Bean
    @Autowired
    public FilterRegistrationBean sessionRepositoryFilterRegistration(
            SessionRepositoryFilter<ExpiringSession> sessionRepositoryFilter) {

        FilterRegistrationBean registration = new FilterRegistrationBean(sessionRepositoryFilter);
        registration.setName("springSessionRepositoryFilter");

        // note : must following order of oAuth2ClientContextFilter
        registration.setOrder(Integer.MAX_VALUE - 1);

        return registration;
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }


    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(SecureRandom.getSeed(10));
        return new BCryptPasswordEncoder(13, secureRandom);
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            // @formatter:off
            resources
                    .resourceId(RESOURCE_ID);
            // @formatter:on
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                    .antMatchers(REST_API.USERS).hasRole("ADMIN")
                    .antMatchers(REST_API.ME).authenticated()
                    .antMatchers(REST_API.ADMIN_POST).authenticated()
                    .antMatchers(REST_API.PERSONAL_POST).authenticated()
                    .antMatchers(REST_API.PUBLIC_POST).authenticated();
//                    .antMatchers(REST_API.WS).authenticated();

//            http.addFilterBefore(sessionRepositoryFilter(), ChannelProcessingFilter.class);
//            http.addFilterBefore(new SessionRepositoryFilter(sessionRepository), ChannelProcessingFilter.class);

            // @formatter:on
            //                    .antMatchers("/stomp").authenticated();
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends
            AuthorizationServerConfigurerAdapter {

        private TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        DataSource dataSource;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.passwordEncoder(passwordEncoder());
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            // @formatter:off
            endpoints
                    .tokenStore(this.tokenStore)
                    .authenticationManager(this.authenticationManager)
                    .userDetailsService(userDetailsService);
            // @formatter:on
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            // @formatter:off .withClientDetails(clientDetailsService);
            clients.jdbc(dataSource);
//                    .inMemory()
//                    .withClient("clientapp")
//                    .authorizedGrantTypes("password", "refresh_token")
//                    .authorities("USER")
//                    .scopes("read", "write")
//                    .resourceIds(RESOURCE_ID)
//                    .secret(passwordEncoder().encode("123456"));
            // @formatter:on
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }

    }

}
