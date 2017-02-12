package com.commercetools.sunrise.shoppingcart.checkout.confirmation.view;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutConfirmationPageContentFactory extends FormPageContentFactory<CheckoutConfirmationPageContent, Cart, CheckoutConfirmationFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;

    @Inject
    public CheckoutConfirmationPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
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
        model.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutConfirmationPageContent model, final Cart cart, final Form<? extends CheckoutConfirmationFormData> form) {
        model.setCheckoutForm(form);
    }
}
