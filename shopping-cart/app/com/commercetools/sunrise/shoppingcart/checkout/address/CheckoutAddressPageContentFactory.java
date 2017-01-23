package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class CheckoutAddressPageContentFactory extends PageContentFactory {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final CheckoutAddressFormSettingsBeanFactory addressFormSettingsFactory;

    @Inject
    public CheckoutAddressPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                             final CheckoutAddressFormSettingsBeanFactory addressFormSettingsFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.addressFormSettingsFactory = addressFormSettingsFactory;
    }

    public CheckoutAddressPageContent create(final Form<?> form, final Cart cart) {
        final CheckoutAddressPageContent bean = new CheckoutAddressPageContent();
        initialize(bean, form, cart);
        return bean;
    }

    protected final void initialize(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        fillTitle(bean, form, cart);
        fillCart(bean, form, cart);
        fillForm(bean, form, cart);
        fillFormSettings(bean, form, cart);
    }

    protected void fillTitle(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        bean.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        bean.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        bean.setAddressForm(form);
    }

    protected void fillFormSettings(final CheckoutAddressPageContent bean, final Form<?> form, final Cart cart) {
        bean.setAddressFormSettings(addressFormSettingsFactory.create(form));
    }
}
