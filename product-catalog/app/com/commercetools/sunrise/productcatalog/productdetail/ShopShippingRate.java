package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingRate;

public class ShopShippingRate extends Base {

    public final String shippingMethodName;
    public final ShippingRate shippingRate;

    public ShopShippingRate(final String shippingMethodName, final ShippingRate shippingRate) {
        this.shippingMethodName = shippingMethodName;
        this.shippingRate = shippingRate;
    }
}
