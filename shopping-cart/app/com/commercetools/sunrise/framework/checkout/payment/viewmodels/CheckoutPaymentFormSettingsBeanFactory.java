package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.framework.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.framework.checkout.payment.PaymentMethodsWithCart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutPaymentFormSettingsBeanFactory extends FormViewModelFactory<CheckoutPaymentFormSettingsBean, PaymentMethodsWithCart, CheckoutPaymentFormData> {

    private final String paymentFormFieldName;
    private final PaymentMethodFormFieldBeanFactory paymentMethodFormFieldBeanFactory;

    @Inject
    public CheckoutPaymentFormSettingsBeanFactory(final Configuration configuration, final PaymentMethodFormFieldBeanFactory paymentMethodFormFieldBeanFactory) {
        this.paymentFormFieldName = configuration.getString("checkout.payment.formFieldName", "payment");
        this.paymentMethodFormFieldBeanFactory = paymentMethodFormFieldBeanFactory;
    }

    @Override
    protected CheckoutPaymentFormSettingsBean getViewModelInstance() {
        return new CheckoutPaymentFormSettingsBean();
    }

    @Override
    public final CheckoutPaymentFormSettingsBean create(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return super.create(paymentMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutPaymentFormSettingsBean model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        fillPaymentMethod(model, paymentMethodsWithCart, form);
    }

    protected void fillPaymentMethod(final CheckoutPaymentFormSettingsBean model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions = new FormFieldWithOptions<>(form.field(paymentFormFieldName), paymentMethodsWithCart.getPaymentMethods());
        model.setPaymentMethod(paymentMethodFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
