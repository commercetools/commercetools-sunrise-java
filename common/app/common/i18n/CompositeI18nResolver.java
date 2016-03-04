package common.i18n;

import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Resolves the i18n messages by trying to resolve them with each given i18n resolver until one of them succeeds.
 * This class allows to override i18n resolvers.
 */
public final class CompositeI18nResolver extends Base implements I18nResolver {
    private final List<I18nResolver> i18nResolvers;

    private CompositeI18nResolver(final List<I18nResolver> i18nResolvers) {
        this.i18nResolvers = i18nResolvers;
    }

    @Override
    public Optional<String> get(final List<Locale> locales, final String bundle, final String key, final Object... args) {
        for (I18nResolver i18nResolver : i18nResolvers) {
            final Optional<String> message = i18nResolver.get(locales, bundle, key, args);
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
