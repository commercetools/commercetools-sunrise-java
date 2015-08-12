package productcatalog.pages;

import common.utils.PriceFormatter;
import productcatalog.models.RichShippingRate;

public class ShippingRateDataBuilder {

    private final PriceFormatter priceFormatter;

    private ShippingRateDataBuilder(final PriceFormatter priceFormatter) {
        this.priceFormatter = priceFormatter;
    }

    public static ShippingRateDataBuilder of(final PriceFormatter priceFormatter) {
        return new ShippingRateDataBuilder(priceFormatter);
    }

    public ShippingRateData build(final RichShippingRate richShippingRate) {
        return new ShippingRateData(
                richShippingRate.shippingMethodName,
                "",
                priceFormatter.format(richShippingRate.shippingRate.getPrice()),
                richShippingRate.shippingRate.getFreeAbove().map(priceFormatter::format).orElse("")
        );
    }
}
