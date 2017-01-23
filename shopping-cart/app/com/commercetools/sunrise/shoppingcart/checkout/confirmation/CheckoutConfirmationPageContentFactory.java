package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class CheckoutConfirmationPageContentFactory extends PageContentFactory {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;

    @Inject
    public CheckoutConfirmationPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
    }

    public CheckoutConfirmationPageContent create(final Form<?> form, final Cart cart) {
        final CheckoutConfirmationPageContent bean = new CheckoutConfirmationPageContent();
        initialize(bean, form, cart);
        return bean;
    }

    protected final void initialize(final CheckoutConfirmationPageContent bean, final Form<?> form, final Cart cart) {
        fillCart(bean, form, cart);
        fillTitle(bean, form, cart);
        fillForm(bean, form, cart);
    }

    protected void fillTitle(final CheckoutConfirmationPageContent bean, final Form<?> form, final Cart cart) {
        bean.setTitle(pageTitleResolver.getOrEmpty("checkout:confirmationPage.title"));
    }

    protected void fillCart(final CheckoutConfirmationPageContent bean, final Form<?> form, final Cart cart) {
        bean.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutConfirmationPageContent bean, final Form<?> form, final Cart cart) {
        bean.setCheckoutForm(form);
    }
}
