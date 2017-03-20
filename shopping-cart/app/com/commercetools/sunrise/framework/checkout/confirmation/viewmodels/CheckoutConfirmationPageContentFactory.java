package com.commercetools.sunrise.framework.checkout.confirmation.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.checkout.confirmation.CheckoutConfirmationFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutConfirmationPageContentFactory extends FormPageContentFactory<CheckoutConfirmationPageContent, Cart, CheckoutConfirmationFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;

    @Inject
    public CheckoutConfirmationPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final CartViewModelFactory getCartViewModelFactory() {
        return cartViewModelFactory;
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
        viewModel.setCart(cartViewModelFactory.create(cart));
    }

    protected void fillForm(final CheckoutConfirmationPageContent viewModel, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        viewModel.setCheckoutForm(form);
    }
}
