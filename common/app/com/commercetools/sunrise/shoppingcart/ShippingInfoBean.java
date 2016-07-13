package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;

public class ShippingInfoBean extends Base {

    private String label;
    private String description;
    private String price;
    private String deliveryDays;

    public ShippingInfoBean() {
    }

    public ShippingInfoBean(final @Nullable CartShippingInfo shippingInfo, final MoneyContext moneyContext) {
        MonetaryAmount shippingPrice = null;
        if (shippingInfo != null) {
            this.label = shippingInfo.getShippingMethodName();
            shippingPrice = shippingInfo.getPrice();
            final Reference<ShippingMethod> shippingMethodRef = shippingInfo.getShippingMethod();
            if (shippingMethodRef != null && shippingMethodRef.getObj() != null) {
                this.description = shippingMethodRef.getObj().getDescription();
            }
        }
        this.price = moneyContext.formatOrZero(shippingPrice);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(final String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }
}
