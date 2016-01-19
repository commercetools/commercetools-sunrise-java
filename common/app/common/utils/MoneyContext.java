package common.utils;

import io.sphere.sdk.products.Price;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;

public interface MoneyContext {
    MonetaryAmount zero();

    String formatOrZero(@Nullable final MonetaryAmount monetaryAmount);

    String formatOrNull(@Nullable final Price price);

    String formatOrNull(@Nullable final MonetaryAmount amountForOneLineItem);

    static MoneyContext of(final CurrencyUnit currency, final Locale locale) {
        return new MoneyContextImpl(currency, locale);
    }
}
