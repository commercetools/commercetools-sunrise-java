package com.commercetools.sunrise.shoppingcart.checkout.payment;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
final class PaymentSettingsImpl implements PaymentSettings {

    private final List<PaymentMethodInfo> paymentMethods;

    PaymentSettingsImpl() {
        this.paymentMethods = singletonList(PaymentMethodInfoBuilder.of()
                .name(LocalizedString.of(Locale.ENGLISH, "Prepaid", Locale.GERMAN, "Vorkasse"))
                .method("prepaid")
                .build());
    }

    @Override
    public CompletionStage<List<PaymentMethodInfo>> getPaymentMethods(final Cart cart) {
        return completedFuture(paymentMethods);
    }
}
