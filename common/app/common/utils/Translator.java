package common.utils;

import io.sphere.sdk.models.LocalizedStrings;

import java.util.List;
import java.util.Locale;

public class Translator {

    final Locale userLanguage;
    final List<Locale> userFallbackLanguages;
    final List<Locale> projectLanguages;

    private Translator(final Locale userLanguage, final List<Locale> userFallbackLanguages, final List<Locale> projectLanguages) {
        this.userLanguage = userLanguage;
        this.userFallbackLanguages = userFallbackLanguages;
        this.projectLanguages = projectLanguages;
    }

    public static Translator of(final Locale userLanguage, final List<Locale> userFallbackLanguages, final List<Locale> projectLanguages) {
        return new Translator(userLanguage, userFallbackLanguages, projectLanguages);
    }

    public String translate(final LocalizedStrings localizedStrings) {
        return localizedStrings.get(userLanguage)
                .orElse(localizedStrings.get(userFallbackLanguages)
                        .orElse(localizedStrings.get(projectLanguages).orElse("")));
    }
}
