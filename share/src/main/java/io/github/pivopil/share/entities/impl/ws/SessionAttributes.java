package io.github.pivopil.share.entities.impl.ws;

import java.io.Serializable;
import java.util.Map;

/**
 * Created on 08.11.16.
 */
public class SessionAttributes implements Serializable {
    private Map<String, Object> attributes;

    public SessionAttributes() {
    }

    public SessionAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
