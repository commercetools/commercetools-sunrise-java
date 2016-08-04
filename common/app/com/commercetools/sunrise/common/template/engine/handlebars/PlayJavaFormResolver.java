package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.forms.ErrorBean;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.github.jknack.handlebars.ValueResolver;
import play.data.Form;
import play.data.validation.ValidationError;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Resolves the values form a {@link Form} instance.
 */
public enum PlayJavaFormResolver implements ValueResolver {
    INSTANCE;

    @Override
    public Object resolve(final Object context, final String name) {
        return context instanceof Form ? resolveImpl((Form<?>) context, name) : UNRESOLVED;
    }

    @Override
    public Object resolve(final Object context) {
        return UNRESOLVED;
    }

    @Override
    public Set<Map.Entry<String, Object>> propertySet(final Object context) {
        return Collections.emptySet();
    }

    private Object resolveImpl(@Nonnull final Form<?> form, final String name) {
        if (name.equals("errors")) {
            return extractErrors(form);
        } else {
            final String value = form.field(name).value();
            return isFalsy(value) ? false : firstNonNull(value, UNRESOLVED);
        }
    }

    private boolean isFalsy(@Nullable final String value) {
        return value != null && value.equals("false");
    }

    private ErrorsBean extractErrors(@Nullable final Form<?> form) {
        final ErrorsBean errorsBean = new ErrorsBean();
        final List<ErrorBean> errorList = new ArrayList<>();
        if (form != null) {
            form.errors().forEach((field, errors) ->
                    errors.forEach(error -> errorList.add(new ErrorBean(errorMessage(error)))));
        }
        errorsBean.setGlobalErrors(errorList);
        return errorsBean;
    }

    private String errorMessage(final ValidationError error) {
        String message = error.message();
        if (!error.key().isEmpty()) {
            message = error.key() + ": " + message;
        }
        return message;
    }
}
