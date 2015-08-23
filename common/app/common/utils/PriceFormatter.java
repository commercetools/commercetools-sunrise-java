package common.utils;

import javax.money.MonetaryAmount;

@FunctionalInterface
public interface PriceFormatter {
    String format(MonetaryAmount price);
}
