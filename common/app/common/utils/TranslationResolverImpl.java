package common.utils;

import io.sphere.sdk.models.LocalizedString;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.singletonList;

public final class TranslationResolverImpl implements TranslationResolver {
    private final List<Locale> locales;

    private TranslationResolverImpl(final List<Locale> locales) {
        this.locales = locales;
    }

    public static TranslationResolverImpl of(final Locale userLanguage) {
        return of(singletonList(userLanguage));
    }

    public static TranslationResolverImpl of(final List<Locale> locales) {
        return new TranslationResolverImpl(locales);
    }

    public static TranslationResolverImpl of(final Locale userLanguage, final List<Locale> userFallbackLanguages, final List<Locale> projectLanguages) {
        final List<Locale> locales = new ArrayList<>(1 + userFallbackLanguages.size() + projectLanguages.size());
        locales.add(userLanguage);
        locales.addAll(userFallbackLanguages);
        locales.addAll(projectLanguages);
        return of(locales);
    }

    /**
     * Finds the best fitting translation trying the following languages in that order:
     *  - the users preferred language
     *  - one of the users fallback languages
     *  - one of the projects languages
     *
     *  Falls back to an empty String if none is found in the former
     * @param localizedString the source to find the translation
     * @return the found translation or an empty String
     */
    @Override
    public String getTranslation(final LocalizedString localizedString) {
        return localizedString.find(locales).orElse("");
    }
}
