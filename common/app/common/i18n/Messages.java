package common.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@FunctionalInterface
public interface Messages {

    Optional<String> get(final String bundle, final String key, final Locale locale);

    default Optional<String> get(final String bundle, final String key, final List<Locale> locales) {
        for (final Locale locale : locales) {
            final Optional<String> message = get(bundle, key, locale);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }
}
