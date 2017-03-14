package io.github.pivopil.share.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created on 21.06.16.
 */
@MappedSuperclass
public class BasicEntity extends EntityWithId {

    private Date created;

    private Date updated;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = new Date();
    }

}
