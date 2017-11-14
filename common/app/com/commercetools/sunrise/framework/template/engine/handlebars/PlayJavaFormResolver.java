package com.commercetools.sunrise.framework.template.engine.handlebars;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.formatters.ErrorFormatter;
import com.commercetools.sunrise.framework.viewmodels.forms.ErrorViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.ErrorsViewModel;
import com.github.jknack.handlebars.ValueResolver;
import play.data.Form;
import play.data.validation.ValidationError;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Resolves the values form a {@link Form} instance.
 */
@RequestScoped
public final class PlayJavaFormResolver implements ValueResolver {

    private final ErrorFormatter errorFormatter;

    @Inject
    public PlayJavaFormResolver(final ErrorFormatter errorFormatter) {
        this.errorFormatter = errorFormatter;
    }

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

    private ErrorsViewModel extractErrors(@Nullable final Form<?> form) {
        final ErrorsViewModel errorsViewModel = new ErrorsViewModel();
        final List<ErrorViewModel> errorList = new ArrayList<>();
        if (form != null) {
            form.errors()
                    .forEach((field, errors) -> errors
                            .forEach(error -> errorList.add(createError(error))));
        }
        errorsViewModel.setGlobalErrors(errorList);
        return errorsViewModel;
    }

    private ErrorViewModel createError(final ValidationError error) {
        final ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.setMessage(errorFormatter.format(error));
        return errorViewModel;
    }
}
