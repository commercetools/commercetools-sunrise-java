package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

final class CustomI18nHelper extends Base implements Helper<String> {
    private final I18nResolver i18n;

    public CustomI18nHelper(final I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocales(options);
        final I18nIdentifier i18nIdentifier = new I18nIdentifier(context);
        return i18n.getOrEmpty(locales, i18nIdentifier.bundle, i18nIdentifier.key, options.hash);
    }

    @SuppressWarnings("unchecked")
    private static List<Locale> getLocales(final Options options) {
        final List<String> languageTags = (List<String>) options.context.get("locales");
        return languageTags.stream()
                .map(Locale::forLanguageTag)
                .collect(toList());
    }

    private static class I18nIdentifier {
        private final String bundle;
        private final String key;

        public I18nIdentifier(final String context) {
            final String[] parts = StringUtils.split(context, ':');
            final boolean usingDefaultBundle = parts.length == 1;
            this.bundle = usingDefaultBundle ? "main" : parts[0];
            this.key = usingDefaultBundle ? context : parts[1];
        }
    }
}
