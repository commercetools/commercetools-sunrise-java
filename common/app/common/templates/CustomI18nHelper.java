package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.i18n.I18nMessages;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

final class CustomI18nHelper extends Base implements Helper<String> {
    private final I18nMessages i18nMessages;

    public CustomI18nHelper(final I18nMessages i18nMessages) {
        this.i18nMessages = i18nMessages;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocales(options);
        final I18nIdentifier i18nIdentifier = new I18nIdentifier(context);
        final String message = resolveMessage(options, i18nIdentifier, locales);
        return replaceParameters(options, message);
    }

    private String resolveMessage(final Options options, final I18nIdentifier i18nIdentifier, final List<Locale> locales) {
        return resolvePluralMessage(options, i18nIdentifier, locales)
                .orElse(i18nMessages.get(i18nIdentifier.bundle, i18nIdentifier.key, locales)
                        .orElse(null));
    }

    private Optional<String> resolvePluralMessage(final Options options, final I18nIdentifier i18nIdentifier, final List<Locale> locales) {
        if (containsPlural(options)) {
            final String pluralizedKey = i18nIdentifier.key + "_plural";
            return i18nMessages.get(i18nIdentifier.bundle, pluralizedKey, locales);
        } else {
            return Optional.empty();
        }
    }

    private boolean containsPlural(final Options options) {
        return options.hash.entrySet().stream()
                .filter(entry -> entry.getKey().equals("count") && entry.getValue() instanceof Number)
                .anyMatch(entry -> ((Number) entry.getValue()).doubleValue() != 1);
    }

    private String replaceParameters(final Options options, final String resolvedValue) {
        String message = StringUtils.defaultString(resolvedValue);
        for (final Map.Entry<String, Object> entry : options.hash.entrySet()) {
            if (entry.getValue() != null) {
                final String parameter = "__" + entry.getKey() + "__";
                message = message.replace(parameter, entry.getValue().toString());
            }
        }
        return message;
    }

    @SuppressWarnings("unchecked")
    private static List<Locale> getLocales(final Options options) {
        final List<String> languageTags = (List<String>) options.context.get("locales");
        return languageTags.stream()
                .map(Locale::forLanguageTag)
                .collect(toList());
    }

    private static class I18nIdentifier {
        private final String key;
        private final String bundle;

        public I18nIdentifier(final String context) {
            final String[] parts = StringUtils.split(context, ':');
            final boolean usingDefaultBundle = parts.length == 1;
            this.bundle = usingDefaultBundle ? "translations" : parts[0];
            this.key = usingDefaultBundle ? context : parts[1];
        }
    }
}
