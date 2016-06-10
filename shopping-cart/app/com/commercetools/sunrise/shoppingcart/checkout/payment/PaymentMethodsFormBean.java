package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PaymentMethodsFormBean {

    private List<PaymentMethodBean> list;

    public PaymentMethodsFormBean() {
    }

    public PaymentMethodsFormBean(final List<PaymentMethodInfo> paymentMethods, final List<String> selectedPaymentMethods,
                                  final UserContext userContext) {
        this.list = paymentMethods.stream()
                .map(paymentMethod -> new PaymentMethodBean(paymentMethod, selectedPaymentMethods, userContext))
                .collect(toList());
    }

    public List<PaymentMethodBean> getList() {
        return list;
    }

    public void setList(final List<PaymentMethodBean> list) {
        this.list = list;
    }
}
