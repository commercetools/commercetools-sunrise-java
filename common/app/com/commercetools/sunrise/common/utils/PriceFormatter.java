package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

@ImplementedBy(PriceFormatterImpl.class)
@FunctionalInterface
public interface PriceFormatter {

    String format(@NotNull final MonetaryAmount monetaryAmount);

}
