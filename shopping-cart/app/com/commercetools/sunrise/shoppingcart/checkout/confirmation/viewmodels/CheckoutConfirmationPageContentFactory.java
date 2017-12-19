package com.commercetools.sunrise.shoppingcart.checkout.confirmation.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutConfirmationPageContentFactory extends FormPageContentFactory<CheckoutConfirmationPageContent, Cart, CheckoutConfirmationFormData> {

    @Inject
    public CheckoutConfirmationPageContentFactory() {
    }

    @Override
    protected CheckoutConfirmationPageContent newViewModelInstance(final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        return new CheckoutConfirmationPageContent();
    }

    @Override
    public final CheckoutConfirmationPageContent create(final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        super.initialize(viewModel, cart, form);
        fillCart(viewModel, cart, form);
        fillForm(viewModel, cart, form);
    }

    protected void fillCart(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setCart(cart);
    }

    protected void fillForm(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setCheckoutForm(form);
    }
}
