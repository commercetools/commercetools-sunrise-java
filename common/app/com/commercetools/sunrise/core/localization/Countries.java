package com.commercetools.sunrise.core.localization;

import com.google.inject.ImplementedBy;
import com.neovisionaries.i18n.CountryCode;

import java.util.List;

/**
 * Manages countries in Sunrise
 */
@ImplementedBy(DefaultCountries.class)
public interface Countries {

    /**
     * The available countries.
     * @return list of available countries
     */
    List<CountryCode> availables();

    /**
     * Select a preferred country, given the list of candidates.
     *
     * Will select the preferred country, based on what countries are available, or return the default country if
     * none of the candidates are available.
     *
     * @param candidates list of candidate countries
     * @return the preferred country
     */
    CountryCode preferred(final List<CountryCode> candidates);
}
