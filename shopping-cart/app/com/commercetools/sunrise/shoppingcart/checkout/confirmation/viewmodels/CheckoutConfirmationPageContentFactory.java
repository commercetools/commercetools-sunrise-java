package com.commercetools.sunrise.shoppingcart.checkout.confirmation.viewmodels;

import com.commercetools.sunrise.core.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutConfirmationPageContentFactory extends FormPageContentFactory<CheckoutConfirmationPageContent, Cart, CheckoutConfirmationFormData> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    public CheckoutConfirmationPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
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

    @Override
    protected void fillTitle(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:confirmationPage.title"));
    }

    protected void fillCart(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setCart(cart);
    }

    protected void fillForm(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setCheckoutForm(form);
    }
}
