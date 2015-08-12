package productcatalog.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingRate;

public class RichShippingRate extends Base {

    public final String shippingMethodName;
    public final ShippingRate shippingRate;

    public RichShippingRate(final String shippingMethodName, final ShippingRate shippingRate) {
        this.shippingMethodName = shippingMethodName;
        this.shippingRate = shippingRate;
    }
}
