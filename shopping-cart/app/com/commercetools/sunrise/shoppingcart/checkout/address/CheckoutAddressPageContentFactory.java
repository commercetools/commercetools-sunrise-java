package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutPageData;

import javax.inject.Inject;

@RequestScoped
public class CheckoutAddressPageContentFactory extends PageContentFactory<CheckoutAddressPageContent, CheckoutPageData> {

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
    public final CheckoutAddressPageContent create(final CheckoutPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutAddressPageContent model, final CheckoutPageData data) {
        super.initialize(model, data);
        fillCart(model, data);
        fillForm(model, data);
        fillFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutAddressPageContent model, final CheckoutPageData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutAddressPageContent model, final CheckoutPageData data) {
        model.setCart(cartBeanFactory.create(data.cart));
    }

    protected void fillForm(final CheckoutAddressPageContent model, final CheckoutPageData data) {
        model.setAddressForm(data.form);
    }

    protected void fillFormSettings(final CheckoutAddressPageContent model, final CheckoutPageData data) {
        model.setAddressFormSettings(addressFormSettingsFactory.create(data.form));
    }
}
