package com.commercetools.sunrise.payments;

import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

public interface PaymentConfiguration {
    List<PaymentMethodInfo> getPaymentMethodInfoList();
}
