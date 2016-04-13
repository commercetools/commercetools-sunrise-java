package productcatalog.productdetail;

import common.utils.PriceFormatter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingRate;

public class ShippingRateBean extends Base {
    private String shippingMethod;
    private String zone;
    private String shippingRate;
    private String freeAbove;

    public ShippingRateBean() {
    }

    public ShippingRateBean(final String shippingMethod, final String zone, final String shippingRate, final String freeAbove) {
        this.shippingMethod = shippingMethod;
        this.zone = zone;
        this.shippingRate = shippingRate;
        this.freeAbove = freeAbove;
    }

    public ShippingRateBean(final PriceFormatter priceFormatter, final ShopShippingRate shopShippingRate) {
        this.shippingMethod = shopShippingRate.shippingMethodName;
        final ShippingRate shippingRate = shopShippingRate.shippingRate;
        this.shippingRate = priceFormatter.format(shippingRate.getPrice());
        if (shippingRate.getFreeAbove() != null) {
            this.freeAbove = priceFormatter.format(shippingRate.getFreeAbove());
        }
    }

    public String getText() {
        return String.format("%s %s %s %s", shippingMethod, zone, shippingRate, freeAbove);
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public String getZone() {
        return zone;
    }

    public String getShippingRate() {
        return shippingRate;
    }

    public String getFreeAbove() {
        return freeAbove;
    }
}
