package io.github.pivopil.share.builders;

import io.github.pivopil.share.entities.BasicEntity;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Date;

/**
 * Created on 06.05.16.
 */
public interface EntityBuilder<T extends BasicEntity, K extends EntityBuilder, V extends ViewModel<T>> {

    K newInstance();

    V buildViewModel();

    K newInstance(T entity);

    K id(Long val);

    K created(Date val);

    K updated(Date val);

    T build();
}
