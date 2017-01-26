package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutPageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import java.util.List;

public class CheckoutPaymentPageData extends CheckoutPageData {

    public final List<PaymentMethodInfo> paymentMethods;

    public CheckoutPaymentPageData(final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        super(form, cart);
        this.paymentMethods = paymentMethods;
    }
}
