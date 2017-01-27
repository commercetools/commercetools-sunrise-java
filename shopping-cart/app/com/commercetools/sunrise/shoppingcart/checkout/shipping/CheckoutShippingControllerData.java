package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.shoppingcart.common.EditableCartControllerData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutShippingControllerData extends EditableCartControllerData<CheckoutShippingFormData> {

    private final List<ShippingMethod> shippingMethods;

    public CheckoutShippingControllerData(final Form<? extends CheckoutShippingFormData> form, final Cart cart,
                                          @Nullable final Cart updatedCart, final List<ShippingMethod> shippingMethods) {
        super(form, cart, updatedCart);
        this.shippingMethods = shippingMethods;
    }

    public List<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    @Override
    public Cart getCart() {
        return super.getCart();
    }

    @Override
    public Form<? extends CheckoutShippingFormData> getForm() {
        return super.getForm();
    }
}
