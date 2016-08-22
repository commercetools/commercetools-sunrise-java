package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.forms.ErrorBean;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
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
public class PlayJavaFormResolver implements ValueResolver {

    private I18nResolver i18nResolver;
    private I18nIdentifierFactory i18nIdentifierFactory;
    private List<Locale> locales;

    public PlayJavaFormResolver(final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory,
                                final List<Locale> locales) {
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.locales = locales;
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

    protected Object resolveImpl(@Nonnull final Form<?> form, final String name) {
        if (name.equals("errors")) {
            return extractErrors(form);
        } else {
            final String value = form.field(name).value();
            return isFalsy(value) ? false : firstNonNull(value, UNRESOLVED);
        }
    }

    protected boolean isFalsy(@Nullable final String value) {
        return value != null && value.equals("false");
    }

    protected ErrorsBean extractErrors(@Nullable final Form<?> form) {
        final ErrorsBean errorsBean = new ErrorsBean();
        final List<ErrorBean> errorList = new ArrayList<>();
        if (form != null) {
            form.errors().forEach((field, errors) ->
                    errors.forEach(error -> errorList.add(new ErrorBean(errorMessage(error)))));
        }
        errorsBean.setGlobalErrors(errorList);
        return errorsBean;
    }

    protected String errorMessage(final ValidationError error) {
        final String message = error.message();
        return i18nResolver.get(locales, i18nIdentifierFactory.create(message))
                .map(localizedMsg -> localizedError(error, localizedMsg))
                .orElseGet(() -> unlocalizedError(error, message));
    }

    protected String localizedError(final ValidationError error, final String message) {
        return unlocalizedError(error, message);
    }

    protected String unlocalizedError(final ValidationError error, final String message) {
        return !error.key().isEmpty() ?  error.key() + ": " + message : message;
    }
}
