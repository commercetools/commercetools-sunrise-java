package com.commercetools.sunrise.common.template.engine.handlebars;

import com.github.jknack.handlebars.ValueResolver;
import play.data.Form;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
        return firstNonNull(form.field(name).valueOr(null), UNRESOLVED);
    }
}
