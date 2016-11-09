package io.github.pivopil.share.persistence.ws;


import io.github.pivopil.share.entities.impl.ws.SessionAttributes;
import io.github.pivopil.share.entities.impl.ws.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Created on 08.11.16.
 */

@Repository
public class JPASessionRepository implements SessionRepository<ExpiringSession> {

    private int defaultMaxInactiveInterval = -1;

    @Autowired
    private SpringSessionRepository springSessionRepository;

    public JPASessionRepository() {
    }

    public JPASessionRepository(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    @Override
    public ExpiringSession createSession() {
        ExpiringSession result = new MapSession();
        result.setMaxInactiveIntervalInSeconds(defaultMaxInactiveInterval);
        return result;
    }

    @Transactional
    @Override
    public void save(ExpiringSession session) {
        springSessionRepository.save(convertToDomain(session));
    }

    @Transactional
    @Override
    public ExpiringSession getSession(String id) {
        SessionEntity sessionEntity = springSessionRepository.findOne(id);
        ExpiringSession saved = sessionEntity == null ? null : convertToSession(sessionEntity);
        if(saved == null) {
            return null;
        }
        if(saved.isExpired()) {
            delete(saved.getId());
            return null;
        }
        return saved;
    }

    @Override
    public void delete(String id) {
        springSessionRepository.delete(id);
    }

    private SessionEntity convertToDomain(ExpiringSession session) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(session.getId());
        sessionEntity.setLastAccessedTime(session.getLastAccessedTime());
        sessionEntity.setCreationTime(session.getCreationTime());
        sessionEntity.setData(serializeAttributes(session));
        return sessionEntity;
    }

    //this serialize attributes of session
    private byte[] serializeAttributes(ExpiringSession session) {
        Map<String, Object> attributes = new HashMap<>();
        for (String attrName : session.getAttributeNames()) {
            attributes.put(attrName, session.getAttribute(attrName));

        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[0];
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(new SessionAttributes(attributes));
            buffer = out.toByteArray();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return buffer;
    }


    private ExpiringSession convertToSession(SessionEntity sessionEntity) {
        MapSession mapSession = new MapSession();
        mapSession.setId(sessionEntity.getId());
        mapSession.setLastAccessedTime(sessionEntity.getLastAccessedTime());
        mapSession.setCreationTime(sessionEntity.getCreationTime());
        mapSession.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);

        SessionAttributes attributes = deserializeAttributes(sessionEntity);
        if (attributes != null) {
            for (Map.Entry<String, Object> attribute : attributes.getAttributes().entrySet()) {
                mapSession.setAttribute(attribute.getKey(), attribute.getValue());
            }
        }
        return mapSession;
    }

    //this deserialize attributes from data
    private SessionAttributes deserializeAttributes(SessionEntity sessionEntity) {
        SessionAttributes attributes = null;
        if(sessionEntity.getData() != null && sessionEntity.getData().length > 0) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(sessionEntity.getData()));
                Object o = objectInputStream.readObject();
                attributes = (SessionAttributes) o;
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return attributes;
    }

    public Integer getDefaultMaxInactiveInterval() {
        return defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public SpringSessionRepository getSpringSessionRepository() {
        return springSessionRepository;
    }

    public void setSpringSessionRepository(SpringSessionRepository springSessionRepository) {
        this.springSessionRepository = springSessionRepository;
    }
}