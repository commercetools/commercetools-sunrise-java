package common.templates.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.i18n.I18nResolver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

final class HandlebarsI18nHelper extends HandlebarsHelperBase implements Helper<String> {
    private final I18nResolver i18n;

    public HandlebarsI18nHelper(final I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocalesFromContext(options);
        final MessageIdentifier messageIdentifier = new MessageIdentifier(context);
        return i18n.getOrEmpty(locales, messageIdentifier.getBundle(), messageIdentifier.getKey(), options.hash);
    }
}
