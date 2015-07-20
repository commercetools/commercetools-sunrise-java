package common.utils;

import common.contexts.AppContext;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

public class PriceFormatterImpl implements PriceFormatter {

    private PriceFormatterImpl() {}

    public static PriceFormatterImpl of() {
        return new PriceFormatterImpl();
    }

    @Override
    public String format(MonetaryAmount price, AppContext context) {
        final MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(context.user().country().toLocale());
        return format.format(price);
    }
}
