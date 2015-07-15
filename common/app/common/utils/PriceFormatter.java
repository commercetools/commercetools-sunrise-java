package common.utils;

import common.contexts.AppContext;

import javax.money.MonetaryAmount;

public interface PriceFormatter {

    String format(final MonetaryAmount price, final AppContext context);
}
