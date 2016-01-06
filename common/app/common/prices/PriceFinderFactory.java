package common.prices;

import common.contexts.UserContext;

public final class PriceFinderFactory {
    private PriceFinderFactory() {
    }

    public static PriceFinder create(final UserContext userContext) {
        return PriceFinder.of(userContext.currency(), userContext.country(), userContext.customerGroup(), userContext.channel());
    }
}
