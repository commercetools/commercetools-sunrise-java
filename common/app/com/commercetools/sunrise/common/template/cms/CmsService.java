package com.commercetools.sunrise.common.template.cms;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Service that provides page content, coming from some sort of Content Management System (CMS).
 */
@FunctionalInterface
public interface CmsService {

     /**
     * Gets the content corresponding to the given CMS identifier for the first found given language.
     * @param locales the list of locales used to translate the message
     * @param cmsIdentifier identifier of the CMS entry field
     * @return the {@code completionStage} of the content in the first found given language, or absent if it could not be found
     */
    CompletionStage<Optional<String>> get(final List<Locale> locales, final CmsIdentifier cmsIdentifier);

    /**
     * Gets the content corresponding to the given CMS identifier for the first found given language.
     * @param locales the list of locales used to translate the message
     * @param cmsIdentifier identifier of the CMS content
     * @return the {@code completionStage} of the content in the first found given language, or empty string if it could not be found
     */
    default CompletionStage<String> getOrEmpty(final List<Locale> locales, final CmsIdentifier cmsIdentifier) {
        return get(locales, cmsIdentifier).thenApply(content -> content.orElse(""));
    }
}
