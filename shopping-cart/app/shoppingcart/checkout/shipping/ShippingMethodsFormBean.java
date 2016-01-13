package shoppingcart.checkout.shipping;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ShippingMethodsFormBean {
    private List<SelectableShippingMethodBean> list;


    public ShippingMethodsFormBean() {
    }

    public ShippingMethodsFormBean(final Cart cart, final ShippingMethods shippingMethods) {
        final String nullableSelectedShippingMethodId = Optional.ofNullable(cart.getShippingInfo())
                .map(s -> s.getShippingMethod().getId())
                .orElse(null);

        fill(shippingMethods, nullableSelectedShippingMethodId);
    }

    private void fill(final ShippingMethods shippingMethods, @Nullable final String selectedShippingMethodId) {
        fill(shippingMethods.getShippingMethods(), selectedShippingMethodId);
    }

    private void fill(final List<ShippingMethod> shippingMethods, @Nullable final String selectedShippingMethodId) {
        final List<SelectableShippingMethodBean> shippingMethodBeanList = shippingMethods.stream()
                .map(shippingMethod -> {
                    final SelectableShippingMethodBean bean = new SelectableShippingMethodBean();
                    bean.setLabel(shippingMethod.getName());
                    bean.setValue(shippingMethod.getId());
                    final Boolean selected = shippingMethod.getId().equals(selectedShippingMethodId);
                    bean.setSelected(selected);
                    return bean;
                })
                .collect(toList());
        setList(shippingMethodBeanList);
    }

    public ShippingMethodsFormBean(final ShippingMethods shippingMethods, final CheckoutShippingFormData checkoutShippingFormData) {
        fill(shippingMethods, checkoutShippingFormData.getShippingMethodId());
    }

    public List<SelectableShippingMethodBean> getList() {
        return list;
    }

    public void setList(final List<SelectableShippingMethodBean> list) {
        this.list = list;
    }
}
