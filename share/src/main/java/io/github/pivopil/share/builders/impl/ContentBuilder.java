package io.github.pivopil.share.builders.impl;

/**
 * Created on 11.01.17.
 */


import io.github.pivopil.share.builders.EntityBuilder;
import io.github.pivopil.share.entities.impl.Content;
import io.github.pivopil.share.viewmodels.impl.ContentViewModel;
import net.sf.oval.Validator;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class ContentBuilder implements EntityBuilder<Content, ContentBuilder, ContentViewModel> {
    private Long id;
    private Date created;
    private Date updated;

    @NotNull
    @NotEmpty
    @Length(min = 1, max = 100)
    private String title;

    private Validator ovalValidator;

    public ContentBuilder() {
    }

    public ContentBuilder(Content entity) {
        id = entity.getId();
        created = entity.getCreated();
        updated = entity.getUpdated();
        title = entity.getTitle();
    }

    @Override
    public ContentBuilder newInstance() {
        return new ContentBuilder();
    }

    @Override
    public ContentViewModel buildViewModel() {
        ContentViewModel contentViewModel = new ContentViewModel();
        contentViewModel.setId(id);
        contentViewModel.setCreated(created);
        contentViewModel.setUpdated(updated);
        contentViewModel.setTitle(title);
        return contentViewModel;
    }

    @Override
    public ContentBuilder newInstance(Content entity) {
        return new ContentBuilder(entity);
    }

    public ContentBuilder id(Long val) {
        id = val;
        return this;
    }

    public ContentBuilder created(Date val) {
        created = val;
        return this;
    }

    public ContentBuilder updated(Date val) {
        updated = val;
        return this;
    }

    public ContentBuilder title(String val) {
        title = val;
        return this;
    }

    @Override
    public Content build() {

        validate(ovalValidator);

        Content content = new Content();
        content.setId(id);
        content.setCreated(created);
        content.setUpdated(updated);
        content.setTitle(title);
        return content;
    }

    @Override
    public ContentBuilder withOvalValidator(Validator ovalValidator) {
        this.ovalValidator = ovalValidator;
        return this;
    }
}
