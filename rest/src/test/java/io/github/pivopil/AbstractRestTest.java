package io.github.pivopil;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created on 15.12.16.
 */
public class AbstractRestTest {

    private static final String DEFAULT_CLIENT_SECRET = "apiOne";
    private static final String DEFAULT_CLIENT_NAME = DEFAULT_CLIENT_SECRET;

    String getAccessToken(String username, String password, MockMvc mvc) throws Exception {
        String authorization = "Basic "
                + new String(Base64Utils.encode((DEFAULT_CLIENT_NAME + ":" + DEFAULT_CLIENT_SECRET).getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

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

        return content.substring(17, 53);
    }

}
