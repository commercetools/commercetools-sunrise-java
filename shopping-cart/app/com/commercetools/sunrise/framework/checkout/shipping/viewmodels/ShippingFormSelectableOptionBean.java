package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;

public class ShippingFormSelectableOptionBean extends FormSelectableOptionBean {

    private String deliveryDays;
    private String price;

    public ShippingFormSelectableOptionBean() {
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
