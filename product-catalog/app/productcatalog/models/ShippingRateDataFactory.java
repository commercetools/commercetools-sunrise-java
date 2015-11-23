package productcatalog.models;

import common.utils.PriceFormatter;

import java.util.Optional;

public class ShippingRateDataFactory {
    private final PriceFormatter priceFormatter;

    private ShippingRateDataFactory(final PriceFormatter priceFormatter) {
        this.priceFormatter = priceFormatter;
    }

    public ShippingRateData create(final ShopShippingRate shopShippingRate) {
        return new ShippingRateData(
                shopShippingRate.shippingMethodName,
                "",
                priceFormatter.format(shopShippingRate.shippingRate.getPrice()),
                Optional.ofNullable(shopShippingRate.shippingRate.getFreeAbove()).map(priceFormatter::format).orElse("")
        );
    }

    public static ShippingRateDataFactory of(final PriceFormatter priceFormatter) {
        return new ShippingRateDataFactory(priceFormatter);
    }
}
