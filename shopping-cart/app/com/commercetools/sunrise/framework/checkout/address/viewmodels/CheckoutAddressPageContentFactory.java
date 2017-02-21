package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.models.carts.CartBeanFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.framework.checkout.address.CheckoutAddressFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutAddressPageContentFactory extends FormPageContentFactory<CheckoutAddressPageContent, Cart, CheckoutAddressFormData> {

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

    @Override
    protected CheckoutAddressPageContent getViewModelInstance() {
        return new CheckoutAddressPageContent();
    }

    @Override
    public final CheckoutAddressPageContent create(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutAddressPageContent model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        super.initialize(model, cart, form);
        fillCart(model, cart, form);
        fillForm(model, cart, form);
        fillFormSettings(model, cart, form);
    }

    @Override
    protected void fillTitle(final CheckoutAddressPageContent model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutAddressPageContent model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutAddressPageContent model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setAddressForm(form);
    }

    protected void fillFormSettings(final CheckoutAddressPageContent model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setAddressFormSettings(addressFormSettingsFactory.create(cart, form));
    }
}
