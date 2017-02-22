package com.commercetools.sunrise.framework.checkout.confirmation.viewmodels;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartViewModelFactory;
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

    @Override
    protected CheckoutConfirmationPageContent getViewModelInstance() {
        return new CheckoutConfirmationPageContent();
    }

    @Override
    public final CheckoutConfirmationPageContent create(final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutConfirmationPageContent model, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        super.initialize(model, cart, form);
        fillCart(model, cart, form);
        fillForm(model, cart, form);
    }

    @Override
    protected void fillTitle(final CheckoutConfirmationPageContent model, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:confirmationPage.title"));
    }

    protected void fillCart(final CheckoutConfirmationPageContent model, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        model.setCart(cartViewModelFactory.create(cart));
    }

    protected void fillForm(final CheckoutConfirmationPageContent model, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        model.setCheckoutForm(form);
    }
}
