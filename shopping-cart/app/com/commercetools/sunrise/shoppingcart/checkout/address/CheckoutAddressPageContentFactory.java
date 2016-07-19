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
        final CheckoutAddressPageContent bean = new CheckoutAddressPageContent();
        initialize(bean, form, cart);
        return bean;
    }

    protected final void initialize(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        fillTitle(bean, cart);
        fillCart(bean, cart);
        fillForm(bean, form);
    }

    protected void fillForm(final CheckoutAddressPageContent bean, final Form<?> form) {
        bean.setAddressFormSettings(addressFormSettingsFactory.create(form));
        bean.setAddressForm(form);
    }

    protected void fillCart(final CheckoutAddressPageContent bean, final Cart cart) {
        bean.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final CheckoutAddressPageContent bean, final Cart cart) {
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }
}
