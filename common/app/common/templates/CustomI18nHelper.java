package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.utils.I18nUtils;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

final class CustomI18nHelper extends Base implements Helper<String> {
    private final I18nUtils i18nUtils;

    public CustomI18nHelper(final List<Locale> locales, final List<String> bundles) {
        this.i18nUtils = new I18nUtils(locales, bundles);
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final String languageTag = getLocales(options).get(0); // TODO improve for multiple languages
        final I18nIdentifier i18nIdentifier = new I18nIdentifier(context);
        final String message = resolveMessage(options, languageTag, i18nIdentifier);
        return replaceParameters(options, message);
    }

    private String resolveMessage(final Options options, final String languageTag, final I18nIdentifier i18nIdentifier) {
        return resolvePluralMessage(options, languageTag, i18nIdentifier)
                .orElseGet(() -> i18nUtils.resolve(languageTag, i18nIdentifier.bundle, i18nIdentifier.key).orElse(null));
    }

    private Optional<String> resolvePluralMessage(final Options options, final String languageTag, final I18nIdentifier i18nIdentifier) {
        if (containsPlural(options)) {
            return i18nUtils.resolve(languageTag, i18nIdentifier.bundle, pluralizedKey(i18nIdentifier.key));
        } else {
            return Optional.empty();
        }
    }

    private String pluralizedKey(final String key) {
        return key + "_plural";
    }

    private String replaceParameters(final Options options, final String resolvedValue) {
        String parametersReplaced = StringUtils.defaultString(resolvedValue);
        for (final Map.Entry<String, Object> entry : options.hash.entrySet()) {
            if (entry.getValue() != null) {
                parametersReplaced = parametersReplaced.replace("__" + entry.getKey() + "__", entry.getValue().toString());
            }
        }
        return parametersReplaced;
    }

    private boolean containsPlural(final Options options) {
        return options.hash.entrySet().stream()
                .anyMatch(entry ->
                        (entry.getValue() instanceof Integer && ((((Number) entry.getValue()).intValue() != 1)))
                                || (entry.getValue() instanceof Long && ((((Number) entry.getValue()).longValue() != 1L))));
    }

    @SuppressWarnings("unchecked")
    private static List<String> getLocales(final Options options) {
        return (List<String>) options.context.get("locales");
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
