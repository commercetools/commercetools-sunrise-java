package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutWithUpdatedCartPageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutPaymentPageData extends CheckoutWithUpdatedCartPageData {

    public final List<PaymentMethodInfo> paymentMethods;

    public CheckoutPaymentPageData(final Form<? extends CheckoutPaymentFormData> form, final Cart cart,
                                   @Nullable final Cart updatedCart, final List<PaymentMethodInfo> paymentMethods) {
        super(form, cart, updatedCart);
        this.paymentMethods = paymentMethods;
    }
}
