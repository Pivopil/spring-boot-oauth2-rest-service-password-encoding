package io.github.pivopil.share.builders;

import io.github.pivopil.share.entities.BasicEntity;

import java.util.Date;

/**
 * Created on 06.05.16.
 */
public interface EntityBuilder<T extends BasicEntity, K extends EntityBuilder> {

    K newInstance();

    K newInstance(T entity);

    K id(Long val);

    K created(Date val);

    K updated(Date val);

    T build();
}
