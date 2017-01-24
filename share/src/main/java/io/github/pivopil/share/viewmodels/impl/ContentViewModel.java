package io.github.pivopil.share.viewmodels.impl;

import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created on 11.01.17.
 */
public class ContentViewModel implements ViewModel<Content> {

    private Long id;

    private Date created;

    private Date updated;

    private String title;
    private String owner;
    private List<String> acls;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setAcls(List<String> acls) {
        this.acls = acls;
    }

    public List<String> getAcls() {
        return acls;
    }
}
