package io.github.pivopil.share.entities.impl.ws;

/**
 * Created on 08.11.16.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class SessionEntity {
    @Id
    private String id;

    private Long creationTime;

    private Long lastAccessedTime;

    @Lob
    @Column(length=100000)
    private byte[] data;

    public SessionEntity() {
    }

    public SessionEntity(String id, long creationTime, long lastAccessedTime, byte[] data) {
        this.id = id;
        this.creationTime = creationTime;
        this.lastAccessedTime = lastAccessedTime;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
