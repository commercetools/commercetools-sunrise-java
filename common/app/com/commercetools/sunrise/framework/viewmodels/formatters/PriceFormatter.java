package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.google.inject.ImplementedBy;

import javax.money.MonetaryAmount;

@ImplementedBy(PriceFormatterImpl.class)
@FunctionalInterface
public interface PriceFormatter {

    /**
     * Formats the given monetary amount into a human-readable version.
     * @param monetaryAmount amount to be formatted
     * @return the formatted version of the given amount
     */
    String format(final MonetaryAmount monetaryAmount);
}
