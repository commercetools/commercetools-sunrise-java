package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutAddressPageContentFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;
    @Inject
    private CheckoutAddressFormSettingsFactory addressFormSettingsFactory;

    public CheckoutAddressPageContent create(final Form<?> form, final Cart cart) {
        return fillBean(new CheckoutAddressPageContent(), form, cart);
    }

    protected <T extends CheckoutAddressPageContent> T fillBean(final T pageContent, final Form<?> form, final Cart cart) {
        fillTitle(pageContent, cart);
        fillCart(pageContent, cart);
        fillForm(pageContent, form);
        return pageContent;
    }

    protected void fillForm(final CheckoutAddressPageContent pageContent, final Form<?> form) {
        pageContent.setAddressFormSettings(addressFormSettingsFactory.create(form));
        pageContent.setAddressForm(form);
    }

    protected void fillCart(final CheckoutAddressPageContent pageContent, final Cart cart) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final CheckoutAddressPageContent pageContent, final Cart cart) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }
}
