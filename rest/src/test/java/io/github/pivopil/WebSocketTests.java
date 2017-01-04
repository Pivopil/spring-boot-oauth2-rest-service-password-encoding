package io.github.pivopil;

import io.github.pivopil.rest.constants.WS_API;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class WebSocketTests extends AbstractRestTest {


    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Value("${local.server.port}")
    private String port;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void run() throws Exception {
//        String accessToken = getAccessToken("adminLogin", "admin", mvc);
//
//        List<Transport> transports = new ArrayList<>(2);
//        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//        transports.add(new RestTemplateXhrTransport());
//
//        SockJsClient sockJsClient = new SockJsClient(transports);
//        ListenableFuture<WebSocketSession> wsSession = sockJsClient.doHandshake(
//                this.webSocketHandler, "ws://localhost:" + this.port + WS_API.HANDSHAKE + "?access_token=" + accessToken);
//
//        this.thrown.expect(ExecutionException.class);
//        wsSession.get().sendMessage(new TextMessage("a"));
    }
}
