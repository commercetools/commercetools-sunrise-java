package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;

import javax.inject.Inject;

@RequestScoped
public class CheckoutPaymentPageContentFactory extends PageContentFactory<CheckoutPaymentPageContent, CheckoutPaymentPageData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final CheckoutPaymentFormSettingsBeanFactory checkoutPaymentFormSettingsBeanFactory;

    @Inject
    public CheckoutPaymentPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                             final CheckoutPaymentFormSettingsBeanFactory checkoutPaymentFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.checkoutPaymentFormSettingsBeanFactory = checkoutPaymentFormSettingsBeanFactory;
    }

    @Override
    protected CheckoutPaymentPageContent getViewModelInstance() {
        return new CheckoutPaymentPageContent();
    }

    @Override
    public final CheckoutPaymentPageContent create(final CheckoutPaymentPageData data) {
        return super.create(data);
    }

    protected final void initialize(final CheckoutPaymentPageContent model, final CheckoutPaymentPageData data) {
        super.initialize(model, data);
        fillCart(model, data);
        fillForm(model, data);
        fillFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutPaymentPageContent model, final CheckoutPaymentPageData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:paymentPage.title"));
    }

    protected void fillCart(final CheckoutPaymentPageContent model, final CheckoutPaymentPageData data) {
        model.setCart(cartBeanFactory.create(data.cart));
    }

    protected void fillForm(final CheckoutPaymentPageContent model, final CheckoutPaymentPageData data) {
        model.setPaymentForm(data.form);
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent model, final CheckoutPaymentPageData data) {
        model.setPaymentFormSettings(checkoutPaymentFormSettingsBeanFactory.create(data));
    }
}
