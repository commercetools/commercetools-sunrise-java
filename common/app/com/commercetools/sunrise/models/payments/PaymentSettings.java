package com.commercetools.sunrise.models.payments;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

@ImplementedBy(PaymentSettingsImpl.class)
public interface PaymentSettings {

    List<PaymentMethodInfo> paymentMethods();
}
