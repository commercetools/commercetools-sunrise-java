package com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.ShippingMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingPageContentFactory extends FormPageContentFactory<CheckoutShippingPageContent, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory) {
        this.checkoutShippingFormSettingsViewModelFactory = checkoutShippingFormSettingsViewModelFactory;
    }

    protected final CheckoutShippingFormSettingsViewModelFactory getCheckoutShippingFormSettingsViewModelFactory() {
        return checkoutShippingFormSettingsViewModelFactory;
    }

    @Override
    protected CheckoutShippingPageContent newViewModelInstance(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return new CheckoutShippingPageContent();
    }

    @Override
    public final CheckoutShippingPageContent create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        super.initialize(viewModel, shippingMethodsWithCart, form);
        fillCart(viewModel, shippingMethodsWithCart, form);
        fillForm(viewModel, shippingMethodsWithCart, form);
        fillFormSettings(viewModel, shippingMethodsWithCart, form);
        viewModel.put("cart", shippingMethodsWithCart.getCart());
    }

    protected void fillCart(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setCart(shippingMethodsWithCart.getCart());
    }

    protected void fillForm(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setShippingForm(form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent viewModel, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        viewModel.setShippingFormSettings(checkoutShippingFormSettingsViewModelFactory.create(shippingMethodsWithCart, form));
    }
}
