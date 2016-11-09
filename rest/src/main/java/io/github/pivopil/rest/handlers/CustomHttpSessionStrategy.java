package io.github.pivopil.rest.handlers;

import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 08.11.16.
 */
public class CustomHttpSessionStrategy implements HttpSessionStrategy {
    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        if (request.getRequestURL().indexOf("/ws") > -1 && request.getQueryString().indexOf("access_token=") > -1) {
            return request.getQueryString().split("access_token=")[1];
        } else {
            return null;
        }
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        System.out.println();
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        System.out.println();
    }
}
