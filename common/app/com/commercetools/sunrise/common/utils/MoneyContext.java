package com.commercetools.sunrise.common.utils;

import io.sphere.sdk.products.PriceLike;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;

public interface MoneyContext {
    MonetaryAmount zero();

    String formatOrZero(@Nullable final MonetaryAmount monetaryAmount);

    String formatOrNull(@Nullable final PriceLike price);

    String formatOrNull(@Nullable final MonetaryAmount amountForOneLineItem);

    static MoneyContext of(final CurrencyUnit currency, final Locale locale) {
        return new MoneyContextImpl(currency, locale);
    }
}
