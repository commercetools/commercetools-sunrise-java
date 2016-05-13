package common.template.engine.handlebars;

import com.github.jknack.handlebars.Context;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Locale;

import static common.template.engine.handlebars.HandlebarsTemplateEngine.LANGUAGE_TAGS_IN_CONTEXT_KEY;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

final class HelperUtils extends Base {

    private HelperUtils() {
    }

    @SuppressWarnings("unchecked")
    public static List<Locale> getLocalesFromContext(final Context context) {
        final Object languageTagsAsObject = context.get(LANGUAGE_TAGS_IN_CONTEXT_KEY);
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
