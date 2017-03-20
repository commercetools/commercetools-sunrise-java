package com.commercetools.sunrise.framework.localization;

import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Locale;

/**
 * Contains information about the user's supported locales.
 */
@ImplementedBy(UserLanguageImpl.class)
public interface UserLanguage {

    /**
     * Primary locale accepted by the user.
     * @return the supported locale
     */
    Locale locale();

    /**
     * List of distinct locales accepted by the user.
     * @return the supported locales
     */
    List<Locale> locales();
}