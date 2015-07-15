package common.utils;

import common.contexts.AppContext;

import javax.money.MonetaryAmount;

public class PriceFormatterImpl implements PriceFormatter {

    private PriceFormatterImpl() {}

    public static PriceFormatterImpl of() {
        return new PriceFormatterImpl();
    }

    @Override
    public String format(MonetaryAmount price, AppContext context) {
        return price.getNumber().toString();
    }
}
