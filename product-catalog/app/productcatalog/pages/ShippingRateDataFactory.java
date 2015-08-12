package productcatalog.pages;

import common.utils.PriceFormatter;
import productcatalog.models.RichShippingRate;

public class ShippingRateDataFactory {

    private final PriceFormatter priceFormatter;

    private ShippingRateDataFactory(final PriceFormatter priceFormatter) {
        this.priceFormatter = priceFormatter;
    }

    public static ShippingRateDataFactory of(final PriceFormatter priceFormatter) {
        return new ShippingRateDataFactory(priceFormatter);
    }

    public ShippingRateData create(final RichShippingRate richShippingRate) {
        return new ShippingRateData(
                richShippingRate.shippingMethodName,
                "",
                priceFormatter.format(richShippingRate.shippingRate.getPrice()),
                richShippingRate.shippingRate.getFreeAbove().map(priceFormatter::format).orElse("")
        );
    }
}
