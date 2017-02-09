package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.models.ViewModel;
import com.github.jknack.handlebars.ValueResolver;
import io.sphere.sdk.models.LocalizedString;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

final class SunriseJavaBeanValueResolver implements ValueResolver {

    private final ValueResolver delegate;
    private final List<Locale> locales;

    SunriseJavaBeanValueResolver(final ValueResolver delegate, final List<Locale> locales) {
        this.delegate = delegate;
        this.locales = locales;
    }

    @Override
    public Object resolve(final Object context, final String name) {
        Object result = delegate.resolve(context, name);
        if (result instanceof LocalizedString) {
            result = resolveLocalizedString((LocalizedString) result);
        } else if (result == UNRESOLVED && context instanceof ViewModel) {
            result = resolveExtendedViewModel((ViewModel) context, name);
        }
        return result;
    }

    private Object resolveLocalizedString(final LocalizedString localizedString) {
        return localizedString.find(locales).orElse("");
    }

    @Nullable
    private Object resolveExtendedViewModel(final ViewModel viewModel, final String name) {
        final Object result = viewModel.get(name);
        return result != null ? result : UNRESOLVED;
    }

    @Override
    public Set<Map.Entry<String, Object>> propertySet(final Object context) {
        return delegate.propertySet(context);
    }

    @Override
    public Object resolve(final Object context) {
        return delegate.resolve(context);
    }
}
