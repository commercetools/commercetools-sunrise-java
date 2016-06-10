package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

public class CheckoutPaymentFormBean extends Base {

    private PaymentMethodsFormBean paymentOptions;
    private ErrorsBean errors;

    public CheckoutPaymentFormBean() {
    }

    public CheckoutPaymentFormBean(final List<PaymentMethodInfo> paymentMethods, final List<String> selectedPaymentMethods,
                                   final UserContext userContext) {
        this.paymentOptions = new PaymentMethodsFormBean(paymentMethods, selectedPaymentMethods, userContext);
    }

    public PaymentMethodsFormBean getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(final PaymentMethodsFormBean paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
