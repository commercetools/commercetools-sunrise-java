package com.commercetools.sunrise.framework.renderers.handlebars;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.github.jknack.handlebars.ValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import io.sphere.sdk.models.LocalizedString;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

@Singleton
public final class SunriseJavaBeanValueResolver implements ValueResolver {

    private final ValueResolver delegate = JavaBeanValueResolver.INSTANCE;
    private final I18nResolver i18nResolver;

    @Inject
    SunriseJavaBeanValueResolver(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
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

    @Override
    public Set<Map.Entry<String, Object>> propertySet(final Object context) {
        return delegate.propertySet(context);
    }

    @Override
    public Object resolve(final Object context) {
        return delegate.resolve(context);
    }

    @Nullable
    private Object resolveExtendedViewModel(final ViewModel viewModel, final String name) {
        final Object result = viewModel.get(name);
        return result != null ? result : UNRESOLVED;
    }

    private String resolveLocalizedString(final LocalizedString localizedString) {
        return i18nResolver.get(localizedString).orElse("");
    }
}
