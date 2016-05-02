package shoppingcart.checkout.shipping;

import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ShippingMethodsFormBean {

    private List<ShippingMethodBean> list;

    public ShippingMethodsFormBean() {
    }

    public ShippingMethodsFormBean(final List<ShippingMethod> shippingMethods, final @Nullable String selectedShippingMethodId) {
        this.list = shippingMethods.stream()
                .map(shippingMethod -> new ShippingMethodBean(shippingMethod, selectedShippingMethodId))
                .collect(toList());
    }

    public List<ShippingMethodBean> getList() {
        return list;
    }

    public void setList(final List<ShippingMethodBean> list) {
        this.list = list;
    }
}
