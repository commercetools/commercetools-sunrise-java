package purchase;

import common.models.SelectableData;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.Optional;

public class SelectableShippingMethodBean extends SelectableData {
    private String deliveryDays;
    private String price;

    public SelectableShippingMethodBean() {
    }

    public SelectableShippingMethodBean(final CartLike<?> cartLike, final MoneyContext moneyContext) {
        final Optional<ShippingMethod> shippingMethodOptional = Optional.ofNullable(cartLike.getShippingInfo())
                .map(CartShippingInfo::getShippingMethod)
                .map(Reference::getObj);
        if (shippingMethodOptional.isPresent()) {
            final ShippingMethod shippingMethod = shippingMethodOptional.get();
            setLabel(shippingMethod.getName());
            setSelected(true);
            setDescription(shippingMethod.getDescription());
            setPrice(moneyContext.formatOrNull(cartLike.getShippingInfo().getPrice()));
        } else {
            setPrice(moneyContext.formatOrZero(null));
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
