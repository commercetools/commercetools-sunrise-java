package shoppingcart.checkout.shipping;

import common.models.SelectableBean;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;

public class ShippingMethodBean extends SelectableBean {

    private String deliveryDays;
    private String price;

    public ShippingMethodBean() {
    }

    public ShippingMethodBean(final ShippingMethod shippingMethod, final @Nullable String selectedShippingMethodId) {
        super(shippingMethod.getName(), shippingMethod.getId(), shippingMethod.getId().equals(selectedShippingMethodId));
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
