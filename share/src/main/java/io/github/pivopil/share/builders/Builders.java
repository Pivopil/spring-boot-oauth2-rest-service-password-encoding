package io.github.pivopil.share.builders;

import io.github.pivopil.share.builders.impl.CompanyBuilder;
import io.github.pivopil.share.builders.impl.ContentBuilder;
import io.github.pivopil.share.builders.impl.UserBuilder;
import io.github.pivopil.share.entities.BasicEntity;
import io.github.pivopil.share.entities.impl.Company;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.entities.impl.User;

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
        map.put(Content.class, new ContentBuilder());
        map.put(Company.class, new CompanyBuilder());
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


}
