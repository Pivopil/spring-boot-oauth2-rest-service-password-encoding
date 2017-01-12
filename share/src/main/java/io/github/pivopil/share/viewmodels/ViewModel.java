package io.github.pivopil.share.viewmodels;

import java.util.Date;

/**
 * Created on 20.12.16.
 */
public interface ViewModel<T> {

    Long getId();

    void setId(Long id);

    Date getCreated();

    void setCreated(Date created);

    Date getUpdated();

    void setUpdated(Date updated);

}
