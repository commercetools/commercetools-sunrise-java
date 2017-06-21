package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(PaymentSettingsImpl.class)
public interface PaymentSettings {

    CompletionStage<List<PaymentMethodInfo>> getPaymentMethods(final Cart cart);
}
