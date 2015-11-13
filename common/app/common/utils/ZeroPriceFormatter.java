package common.utils;

import javax.money.MonetaryAmount;

public interface ZeroPriceFormatter extends PriceFormatter {
    @Override
    String format(MonetaryAmount monetaryAmount);

    @Override
    String formatOrEmpty(MonetaryAmount monetaryAmount);

    String formatOrZero(MonetaryAmount monetaryAmount);
}
