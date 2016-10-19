package io.github.pivopil.share.entities.impl.domain;

import io.github.pivopil.share.entities.BasicEntity;

import javax.persistence.Entity;

/**
 * Created on 18.10.16.
 */
@Entity
public class AdminPost extends BasicEntity {
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
