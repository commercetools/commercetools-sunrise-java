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

    /**
     * Finds the best fitting translation trying the following languages in that order:
     *  - the users preferred language
     *  - one of the users fallback languages
     *  - one of the projects languages
     *
     *  Falls back to an empty String if none is found in the former
     * @param localizedStrings the source to find the translation
     * @return the found translation or an empty String
     */
    public String findTranslation(final LocalizedStrings localizedStrings) {
        return localizedStrings.get(userLanguage)
                .orElse(localizedStrings.get(userFallbackLanguages)
                        .orElse(localizedStrings.get(projectLanguages).orElse("")));
    }
}
