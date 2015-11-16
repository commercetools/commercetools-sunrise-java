package common.utils;

import io.sphere.sdk.models.Base;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

final class PriceFormatterImpl extends Base implements PriceFormatter {

    private final Locale locale;

    PriceFormatterImpl(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String format(MonetaryAmount monetaryAmount) {
        final AmountFormatQuery amountFormatQuery = AmountFormatQueryBuilder.of(locale)
                .set("pattern", "###0.00### ¤")
                .set(CurrencyStyle.SYMBOL)
                .build();
        final MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(amountFormatQuery);
        return amountFormat.format(monetaryAmount)
                .replace("EUR", "€");//yep, the money lib does not know the euro sign
    }
}
