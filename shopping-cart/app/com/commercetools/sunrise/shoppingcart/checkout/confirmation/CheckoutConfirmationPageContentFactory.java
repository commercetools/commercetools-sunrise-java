package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;

import javax.inject.Inject;

public class CheckoutConfirmationPageContentFactory extends PageContentFactory<CheckoutConfirmationPageContent, CheckoutConfirmationControllerData> {

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
    public final CheckoutConfirmationPageContent create(final CheckoutConfirmationControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutConfirmationPageContent model, final CheckoutConfirmationControllerData data) {
        fillCart(model, data);
        fillTitle(model, data);
        fillForm(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutConfirmationPageContent model, final CheckoutConfirmationControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:confirmationPage.title"));
    }

    protected void fillCart(final CheckoutConfirmationPageContent model, final CheckoutConfirmationControllerData data) {
        model.setCart(cartBeanFactory.create(data.getCart()));
    }

    protected void fillForm(final CheckoutConfirmationPageContent model, final CheckoutConfirmationControllerData data) {
        model.setCheckoutForm(data.getForm());
    }
}
