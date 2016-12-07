package io.github.pivopil.rest.constants;

/**
 * Created on 06.12.16.
 */
public class WS_API {
    private WS_API() {

    }

    public static final String HANDSHAKE = "/messages";
    public static final String INSTANT_MESSAGE = "/im";
    public static final String ACTIVE_USERS = "/users";
    public static final String QUEUE_MESSAGES = "/queue/messages";

    public static final String QUEUE_DESTINATION_PREFIX = "/queue/";
    public static final String TOPIC_DESTINATION_PREFIX = "/topic/";
    public static final String APP_PREFIX = "/app";
    public static final String TOPIC_FRIENDS_SIGNIN = "/topic/friends/signin";
    public static final String TOPIC_FRIENDS_SIGNOUT = "/topic/friends/signout";
}
