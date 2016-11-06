package io.github.pivopil.rest.services;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 02.11.16.
 */

@Service
public class WebSocketService {

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());

    public boolean addSession(WebSocketSession session) {
        return sessions.add(session);
    }

    public boolean removeSession(WebSocketSession session) {
        return sessions.remove(session);
    }

    public void broadcastCurrentData(final String currentData) {

        synchronized (sessions) {
            sessions.stream().filter(WebSocketSession::isOpen).forEach(s -> {
                try {
                    s.sendMessage(new TextMessage(currentData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
