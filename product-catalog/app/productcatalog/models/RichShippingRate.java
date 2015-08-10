package productcatalog.models;

import io.sphere.sdk.shippingmethods.ShippingRate;

public class RichShippingRate {

    public final String shippingMethodName;
    public final ShippingRate shippingRate;

    public RichShippingRate(final String shippingMethodName, final ShippingRate shippingRate) {
        this.shippingMethodName = shippingMethodName;
        this.shippingRate = shippingRate;
    }
}
