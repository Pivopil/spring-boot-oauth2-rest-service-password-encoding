package io.github.pivopil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.controllers.UserController;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest extends AbstractRestTest {


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @InjectMocks
    private UserController controller;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void meAuthorized() throws Exception {
        String accessToken = getAccessToken("adminLogin", "admin", mvc);

        mvc.perform(get(REST_API.USERS + REST_API.ME)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void meUnauthorized() throws Exception {
        mvc.perform(get(REST_API.USERS + REST_API.ME)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
    }

    @Test
    public void adminCreateNeoLocalAdminAndChangeHisName() throws Exception {
        String accessToken = getAccessToken("adminLogin", "admin", mvc);


        //admin get all users
        String contentAsString = mvc.perform(get(REST_API.USERS)
                .header("Authorization", "Bearer " + accessToken))
                .andReturn().getResponse().getContentAsString();

        List<User> userList = mapper.readValue(contentAsString, new TypeReference<List<User>>() {
        });

        boolean isNeoAdminLogin2Exist = userList.stream().filter(u -> u.getLogin().equals("neoAdminLogin2")).count() > 0;

        if (!isNeoAdminLogin2Exist) {
            Role roleOrgNeoAdmin = roleRepository.findOneByName("ROLE_NEO_LOCAL_ADMIN");

            User adminNeo2 = new User();
            adminNeo2.setName("neoAdminName2");
            adminNeo2.setLogin("neoAdminLogin2");
            adminNeo2.setEmail("neoadmin2@email.com");
            adminNeo2.setPhone("9123456782");
            adminNeo2.setPassword("neoAdmin2");
            adminNeo2.setEnabled(Boolean.TRUE);
            adminNeo2.setRoles(new HashSet<>(Collections.singletonList(roleOrgNeoAdmin)));

            String writeValueAsString = mapper.writeValueAsString(adminNeo2);

            String singleContentAsString = mvc.perform(post(REST_API.USERS)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .content(writeValueAsString))
                    .andReturn().getResponse().getContentAsString();

            System.out.printf(singleContentAsString);
        }


    }

    @Test
    public void neoLocalAdminCreateNeoLocalAndChangeHisName() throws Exception {

    }

    @Test
    public void neoLocalUserCanNotChangeNeoLocalAdmin() throws Exception {

    }

    // admin see all users

    // neo local admin see all neo users

    // neo local user see all neo users


}
