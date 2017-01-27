package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.shoppingcart.common.EditableCartControllerData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutPaymentControllerData extends EditableCartControllerData<CheckoutPaymentFormData> {

    private final List<PaymentMethodInfo> paymentMethods;

    public CheckoutPaymentControllerData(final Form<? extends CheckoutPaymentFormData> form, final Cart cart,
                                         @Nullable final Cart updatedCart, final List<PaymentMethodInfo> paymentMethods) {
        super(form, cart, updatedCart);
        this.paymentMethods = paymentMethods;
    }

    public List<PaymentMethodInfo> getPaymentMethods() {
        return paymentMethods;
    }

    @Override
    public Cart getCart() {
        return super.getCart();
    }

    @Override
    public Form<? extends CheckoutPaymentFormData> getForm() {
        return super.getForm();
    }
}
