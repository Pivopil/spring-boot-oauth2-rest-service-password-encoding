package io.github.pivopil.share.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Created on 05.02.17.
 */

@MappedSuperclass
public class EntityWithId {

    @Id
    @GenericGenerator(name = "native_generator", strategy = "native")
    @GeneratedValue(generator = "native_generator")
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityWithId base = (EntityWithId) o;
        return Objects.equals(id, base.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
