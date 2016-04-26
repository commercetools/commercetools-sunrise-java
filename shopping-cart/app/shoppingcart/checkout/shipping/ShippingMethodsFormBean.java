package shoppingcart.checkout.shipping;

import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ShippingMethodsFormBean {

    private List<ShippingMethodBean> list;

    public ShippingMethodsFormBean() {
    }

    public ShippingMethodsFormBean(final List<ShippingMethod> shippingMethods, final @Nullable CartShippingInfo shippingInfo) {
        final String selectedShippingMethodId = Optional.ofNullable(shippingInfo)
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId))
                .orElse(null);
        this.list = shippingMethodsToBean(shippingMethods, selectedShippingMethodId);
    }

    public ShippingMethodsFormBean(final List<ShippingMethod> shippingMethods, final @Nullable String selectedShippingMethodId) {
        this.list = shippingMethodsToBean(shippingMethods, selectedShippingMethodId);
    }

    public List<ShippingMethodBean> getList() {
        return list;
    }

    public void setList(final List<ShippingMethodBean> list) {
        this.list = list;
    }

    private static List<ShippingMethodBean> shippingMethodsToBean(final List<ShippingMethod> shippingMethods,
                                                                  final @Nullable String selectedShippingMethodId) {
        return shippingMethods.stream()
                .map(shippingMethod -> new ShippingMethodBean(shippingMethod, selectedShippingMethodId))
                .collect(toList());
    }
}
