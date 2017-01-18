package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;

import javax.money.MonetaryAmount;

@ImplementedBy(PriceFormatterImpl.class)
@FunctionalInterface
public interface PriceFormatter {

    String format(final MonetaryAmount monetaryAmount);
}
