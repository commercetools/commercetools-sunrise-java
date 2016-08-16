package com.commercetools.sunrise.common.template.i18n.composite;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Base;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Resolves the i18n messages by trying to resolve them with each given i18n resolver until one of them succeeds.
 * This class allows to override i18n resolvers.
 */
@Singleton
public final class CompositeI18nResolver extends Base implements I18nResolver {

    private final List<I18nResolver> i18nResolvers;

    private CompositeI18nResolver(final List<I18nResolver> i18nResolvers) {
        this.i18nResolvers = i18nResolvers;
    }

    @Override
    public Optional<String> get(final List<Locale> locales, final I18nIdentifier i18nIdentifier, final Map<String, Object> hashArgs) {
        for (I18nResolver i18nResolver : i18nResolvers) {
            final Optional<String> message = i18nResolver.get(locales, i18nIdentifier, hashArgs);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }

    public static CompositeI18nResolver of(final List<I18nResolver> i18nResolvers) {
        return new CompositeI18nResolver(i18nResolvers);
    }
}
