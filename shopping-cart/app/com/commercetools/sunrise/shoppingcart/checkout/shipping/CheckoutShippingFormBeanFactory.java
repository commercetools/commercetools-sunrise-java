package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;
import java.util.Optional;

public class CheckoutShippingFormBeanFactory extends Base {
    public CheckoutShippingFormBean create(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final String selectedShippingMethodId = Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId))
                .orElse(null);
        return create(shippingMethods, selectedShippingMethodId);
    }

    public CheckoutShippingFormBean create(final List<ShippingMethod> shippingMethods, final String selectedShippingMethodId) {
        final CheckoutShippingFormBean bean = new CheckoutShippingFormBean();
        bean.setShippingMethods(new ShippingMethodsFormBean(shippingMethods, selectedShippingMethodId));
        return bean;
    }
}
