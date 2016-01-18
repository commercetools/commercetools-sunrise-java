package shoppingcart.checkout.shipping;

import common.models.SelectableData;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

public class SelectableShippingMethodBean extends SelectableData {
    private String deliveryDays;
    private String price;

    public SelectableShippingMethodBean() {
    }

    public SelectableShippingMethodBean(final CartLike<?> cartLike, final MoneyContext moneyContext) {
        final CartShippingInfo shippingInfo = cartLike.getShippingInfo();
        if (shippingInfo != null) {
            this.price = moneyContext.formatOrZero(shippingInfo.getPrice());
            setLabel(shippingInfo.getShippingMethodName());
            setSelected(true);
            final Reference<ShippingMethod> shippingMethodReference = shippingInfo.getShippingMethod();
            if (shippingMethodReference != null && shippingMethodReference.getObj() != null) {
                setDescription(shippingMethodReference.getObj().getDescription());
            }
        } else {
            this.price = moneyContext.formatOrZero(null);
        }
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
