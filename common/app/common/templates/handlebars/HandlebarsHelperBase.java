package common.templates.handlebars;

import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Locale;

import static common.templates.handlebars.HandlebarsTemplateService.LANGUAGE_TAGS_IN_CONTEXT_KEY;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

abstract class HandlebarsHelperBase extends Base {

    @SuppressWarnings("unchecked")
    protected static List<Locale> getLocalesFromContext(final Options options) {
        final Object languageTagsAsObject = options.context.get(LANGUAGE_TAGS_IN_CONTEXT_KEY);
        if (languageTagsAsObject instanceof List) {
            final List<String> locales = (List<String>) languageTagsAsObject;
            return locales.stream()
                    .map(Locale::forLanguageTag)
                    .collect(toList());
        } else {
            return emptyList();
        }
    }
}
