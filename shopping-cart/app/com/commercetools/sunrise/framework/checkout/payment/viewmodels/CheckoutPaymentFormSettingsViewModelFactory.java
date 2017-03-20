package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.viewmodels.FormViewModelFactory;
import com.commercetools.sunrise.framework.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.framework.checkout.payment.PaymentMethodsWithCart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutPaymentFormSettingsViewModelFactory extends FormViewModelFactory<CheckoutPaymentFormSettingsViewModel, PaymentMethodsWithCart, CheckoutPaymentFormData> {

    private final String paymentFormFieldName;
    private final PaymentMethodFormFieldViewModelFactory paymentMethodFormFieldViewModelFactory;

    @Inject
    public CheckoutPaymentFormSettingsViewModelFactory(final Configuration configuration, final PaymentMethodFormFieldViewModelFactory paymentMethodFormFieldViewModelFactory) {
        this.paymentFormFieldName = configuration.getString("checkout.payment.formFieldName", "payment");
        this.paymentMethodFormFieldViewModelFactory = paymentMethodFormFieldViewModelFactory;
    }

    protected final String getPaymentFormFieldName() {
        return paymentFormFieldName;
    }

    protected final PaymentMethodFormFieldViewModelFactory getPaymentMethodFormFieldViewModelFactory() {
        return paymentMethodFormFieldViewModelFactory;
    }

    @Override
    protected CheckoutPaymentFormSettingsViewModel newViewModelInstance(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return new CheckoutPaymentFormSettingsViewModel();
    }

    @Override
    public final CheckoutPaymentFormSettingsViewModel create(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return super.create(paymentMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutPaymentFormSettingsViewModel viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        fillPaymentMethod(viewModel, paymentMethodsWithCart, form);
    }

    protected void fillPaymentMethod(final CheckoutPaymentFormSettingsViewModel viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions = FormFieldWithOptions.of(form.field(paymentFormFieldName), paymentMethodsWithCart.getPaymentMethods());
        viewModel.setPaymentMethod(paymentMethodFormFieldViewModelFactory.create(formFieldWithOptions));
    }
}
