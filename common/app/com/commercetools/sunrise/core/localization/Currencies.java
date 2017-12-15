package com.commercetools.sunrise.core.localization;

import com.google.inject.ImplementedBy;

import javax.money.CurrencyUnit;
import java.util.List;

/**
 * Manages currencies in Sunrise
 */
@ImplementedBy(DefaultCurrencies.class)
public interface Currencies {

    /**
     * The available currencies.
     * @return list of available currencies
     */
    List<CurrencyUnit> availables();

    /**
     * Select a preferred currency, given the list of candidates.
     *
     * Will select the preferred currency, based on what currencies are available, or return the default currency if
     * none of the candidates are available.
     *
     * @param candidates list of candidate currencies
     * @return the preferred currency
     */
    CurrencyUnit preferred(final List<CurrencyUnit> candidates);
}
