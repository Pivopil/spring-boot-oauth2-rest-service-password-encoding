package io.github.pivopil;

/**
 * Created on 07.07.16.
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.controllers.UserController;
import io.github.pivopil.share.entities.impl.Content;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest {

    public static final String DEFAULT_CLIENT_SECRET = "apiOne";
    public static final String DEFAULT_CLIENT_NAME = DEFAULT_CLIENT_SECRET;
    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper mapper;

    @InjectMocks
    UserController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    private String getAccessToken(String username, String password) throws Exception {
        String authorization = "Basic "
                + new String(Base64Utils.encode((DEFAULT_CLIENT_NAME + ":" + DEFAULT_CLIENT_SECRET).getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        // @formatter:off
        String content = mvc
                .perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write")
                                .param("client_id", DEFAULT_CLIENT_NAME)
                                .param("client_secret", DEFAULT_CLIENT_SECRET))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
                .andReturn().getResponse().getContentAsString();

        // @formatter:on

        return content.substring(17, 53);
    }

    @Test
    public void meAuthorized() throws Exception {
        String accessToken = getAccessToken("adminLogin", "admin");

        // @formatter:off
        mvc.perform(get(REST_API.ME)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(jsonPath("$.id", is(notNullValue())));
        // @formatter:on
    }

    @Test
    public void meUnauthorized() throws Exception {
        // @formatter:off
        mvc.perform(get(REST_API.ME)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
        // @formatter:on
    }

    @Test
    public void adminTryToGetContentByTitle() throws Exception {
        String accessToken = getAccessToken("adminLogin", "admin");

        String contentAsString = mvc.perform(get(REST_API.CONTENT + "?title=adminLogin")
                .header("Authorization", "Bearer " + accessToken)).andReturn().getResponse().getContentAsString();

        if (contentAsString.equals("[]")) {
            // add one content object by admin
            Content content = new Content();
            content.setTitle("adminLogin");
            String singleContentAsString = mvc.perform(post(REST_API.CONTENT)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .content(mapper.writeValueAsString(content)))
                    .andExpect(jsonPath("$.title", is(equalTo("adminLogin"))))
                    .andReturn().getResponse().getContentAsString();

            content = mapper.readValue(singleContentAsString, Content.class);

            // remove one content object by admin
            mvc.perform(delete(REST_API.CONTENT + "/" + content.getId()).header("Authorization", "Bearer " + accessToken));

        } else {
            List<Content> contentList = mapper.readValue(contentAsString, new TypeReference<List<Content>>() {
            });
            Content content = contentList.get(0);
            mvc.perform(delete(REST_API.CONTENT + "/" + content.getId()).header("Authorization", "Bearer " + accessToken)).andExpect(status().isNoContent());
        }
    }

}
