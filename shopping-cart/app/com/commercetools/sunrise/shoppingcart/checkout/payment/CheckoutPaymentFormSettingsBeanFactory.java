package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.Configuration;

import javax.inject.Inject;

@RequestScoped
public class CheckoutPaymentFormSettingsBeanFactory extends ViewModelFactory<CheckoutPaymentFormSettingsBean, CheckoutPaymentControllerData> {

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
    public final CheckoutPaymentFormSettingsBean create(final CheckoutPaymentControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutPaymentFormSettingsBean model, final CheckoutPaymentControllerData data) {
        fillPaymentMethod(model, data);
    }

    protected void fillPaymentMethod(final CheckoutPaymentFormSettingsBean model, final CheckoutPaymentControllerData data) {
        final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions = new FormFieldWithOptions<>(data.getForm().field(paymentFormFieldName), data.getPaymentMethods());
        model.setPaymentMethod(paymentMethodFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
