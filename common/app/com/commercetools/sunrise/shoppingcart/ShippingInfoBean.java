package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ViewModel;

public class ShippingInfoBean extends ViewModel {

    private String label;
    private String description;
    private String price;
    private String deliveryDays;

    public ShippingInfoBean() {
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
