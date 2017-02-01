package io.github.pivopil.share.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;

@Entity
public class ActiveWebSocketUser {
    @Id
    @GenericGenerator(name = "native_generator", strategy = "native")
    @GeneratedValue(generator = "native_generator")
    private String id;

    private String username;

    private Calendar connectionTime;

    public ActiveWebSocketUser() {
    }

    public ActiveWebSocketUser(String id, String username, Calendar connectionTime) {
        super();
        this.id = id;
        this.username = username;
        this.connectionTime = connectionTime;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Calendar getConnectionTime() {
        return this.connectionTime;
    }

    public void setConnectionTime(Calendar connectionTime) {
        this.connectionTime = connectionTime;
    }

}
