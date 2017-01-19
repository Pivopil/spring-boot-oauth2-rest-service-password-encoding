package io.github.pivopil.share.builders;

import io.github.pivopil.share.entities.BasicEntity;
import io.github.pivopil.share.exceptions.ExceptionAdapter;
import io.github.pivopil.share.viewmodels.ViewModel;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.Date;
import java.util.List;

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

    K withOvalValidator(Validator ovalValidator);

    default void validate(Validator ovalValidator) {
        if (ovalValidator != null) {
            List<ConstraintViolation> constraintViolations = ovalValidator.validate(this);
            if (constraintViolations.size() > 0) {
                String allMessages = "";
                for (ConstraintViolation constraintViolation : constraintViolations) {
                    allMessages = allMessages + constraintViolation.getMessage();
                }
                throw new ExceptionAdapter(new RuntimeException("Invalid object"));
            }
        }
    }
}
