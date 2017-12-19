package com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels;

import com.commercetools.sunrise.core.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.shoppingcart.checkout.payment.PaymentMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutPaymentPageContentFactory extends FormPageContentFactory<CheckoutPaymentPageContent, PaymentMethodsWithCart, CheckoutPaymentFormData> {

    private final CheckoutPaymentFormSettingsViewModelFactory checkoutPaymentFormSettingsViewModelFactory;

    @Inject
    public CheckoutPaymentPageContentFactory(final CheckoutPaymentFormSettingsViewModelFactory checkoutPaymentFormSettingsViewModelFactory) {
        this.checkoutPaymentFormSettingsViewModelFactory = checkoutPaymentFormSettingsViewModelFactory;
    }

    protected final CheckoutPaymentFormSettingsViewModelFactory getCheckoutPaymentFormSettingsViewModelFactory() {
        return checkoutPaymentFormSettingsViewModelFactory;
    }

    @Override
    protected CheckoutPaymentPageContent newViewModelInstance(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return new CheckoutPaymentPageContent();
    }

    @Override
    public final CheckoutPaymentPageContent create(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return super.create(paymentMethodsWithCart, form);
    }

    protected final void initialize(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        super.initialize(viewModel, paymentMethodsWithCart, form);
        fillCart(viewModel, paymentMethodsWithCart, form);
        fillForm(viewModel, paymentMethodsWithCart, form);
        fillFormSettings(viewModel, paymentMethodsWithCart, form);
    }

    protected void fillCart(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        viewModel.setCart(paymentMethodsWithCart.getCart());
    }

    protected void fillForm(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        viewModel.setPaymentForm(form);
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        viewModel.setPaymentFormSettings(checkoutPaymentFormSettingsViewModelFactory.create(paymentMethodsWithCart, form));
    }
}
