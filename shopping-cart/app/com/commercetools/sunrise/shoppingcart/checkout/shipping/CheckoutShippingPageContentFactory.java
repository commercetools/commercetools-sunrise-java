package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;

import javax.inject.Inject;

@RequestScoped
public class CheckoutShippingPageContentFactory extends PageContentFactory<CheckoutShippingPageContent, CheckoutShippingPageData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final CheckoutShippingFormSettingsBeanFactory checkoutShippingFormSettingsBeanFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                              final CheckoutShippingFormSettingsBeanFactory checkoutShippingFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.checkoutShippingFormSettingsBeanFactory = checkoutShippingFormSettingsBeanFactory;
    }

    @Override
    protected CheckoutShippingPageContent getViewModelInstance() {
        return null;
    }

    @Override
    public final CheckoutShippingPageContent create(final CheckoutShippingPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutShippingPageContent model, final CheckoutShippingPageData data) {
        super.initialize(model, data);
        fillCart(model, data);
        fillForm(model, data);
        fillFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutShippingPageContent model, final CheckoutShippingPageData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutShippingPageContent model, final CheckoutShippingPageData data) {
        model.setCart(cartBeanFactory.create(data.cart));
    }

    protected void fillForm(final CheckoutShippingPageContent model, final CheckoutShippingPageData data) {
        model.setShippingForm(data.form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent model, final CheckoutShippingPageData data) {
        model.setShippingFormSettings(checkoutShippingFormSettingsBeanFactory.create(data));
    }
}
