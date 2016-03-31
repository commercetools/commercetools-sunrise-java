package common.templates.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.i18n.I18nIdentifier;
import common.i18n.I18nResolver;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static common.templates.handlebars.HelperUtils.getLocalesFromContext;

final class HandlebarsI18nHelper extends Base implements Helper<String> {
    private final I18nResolver i18n;

    public HandlebarsI18nHelper(final I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocalesFromContext(options.context);
        final I18nIdentifier i18nIdentifier = I18nIdentifier.of(context);
        return i18n.getOrEmpty(locales, i18nIdentifier, options.hash);
    }
}
