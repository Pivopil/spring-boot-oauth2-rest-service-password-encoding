package io.github.pivopil.share.builders;

import io.github.pivopil.share.builders.impl.UserBuilder;
import io.github.pivopil.share.entities.BasicEntity;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.viewmodels.ViewModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 06.05.16.
 */

public class Builders {
    private Builders() {
    }

    private static Map<Class<? extends BasicEntity>, ? super EntityBuilder> map = new ConcurrentHashMap<>();

    static {
        map.put(User.class, new UserBuilder());
    }

    @SuppressWarnings("unchecked")
    public static <K extends EntityBuilder> K of(Class<? extends BasicEntity> clazz) {
        K builder = (K) map.get(clazz);
        if (builder == null) throw new IllegalArgumentException("No builder for entity: " + clazz.getName());
        return (K) builder.newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasicEntity, K extends EntityBuilder> K of(T entity) {
        if (entity == null) throw new IllegalArgumentException("Entity should not be a null.");

        K builder = (K) map.get(entity.getClass());

        if (builder == null)
            throw new IllegalArgumentException("No builder for entity: " + entity.getClass().getName());

        return (K) builder.newInstance(entity);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasicEntity, K extends EntityBuilder, V extends ViewModel<T>> K of(V viewModel, Class<? extends BasicEntity> clazz) {
        K builder = (K) map.get(clazz);
        if (builder == null) throw new IllegalArgumentException("No builder for entity: " + clazz.getName());
        if (viewModel == null) throw new IllegalArgumentException("Entity should not be a null.");
        return (K) builder.newInstance(viewModel);
    }


}
