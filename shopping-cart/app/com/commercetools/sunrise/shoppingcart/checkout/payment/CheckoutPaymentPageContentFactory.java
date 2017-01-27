package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;

import javax.inject.Inject;

@RequestScoped
public class CheckoutPaymentPageContentFactory extends PageContentFactory<CheckoutPaymentPageContent, CheckoutPaymentControllerData> {

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
    public final CheckoutPaymentPageContent create(final CheckoutPaymentControllerData data) {
        return super.create(data);
    }

    protected final void initialize(final CheckoutPaymentPageContent model, final CheckoutPaymentControllerData data) {
        super.initialize(model, data);
        fillCart(model, data);
        fillForm(model, data);
        fillFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutPaymentPageContent model, final CheckoutPaymentControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:paymentPage.title"));
    }

    protected void fillCart(final CheckoutPaymentPageContent model, final CheckoutPaymentControllerData data) {
        model.setCart(cartBeanFactory.create(data.getCart()));
    }

    protected void fillForm(final CheckoutPaymentPageContent model, final CheckoutPaymentControllerData data) {
        model.setPaymentForm(data.getForm());
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent model, final CheckoutPaymentControllerData data) {
        model.setPaymentFormSettings(checkoutPaymentFormSettingsBeanFactory.create(data));
    }
}
