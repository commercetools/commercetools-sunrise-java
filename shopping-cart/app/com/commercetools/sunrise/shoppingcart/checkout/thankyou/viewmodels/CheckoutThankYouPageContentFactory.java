package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.OrderBeanFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class CheckoutThankYouPageContentFactory extends PageContentFactory<CheckoutThankYouPageContent, Order> {

    private final PageTitleResolver pageTitleResolver;
    private final OrderBeanFactory orderBeanFactory;

    @Inject
    public CheckoutThankYouPageContentFactory(final PageTitleResolver pageTitleResolver, final OrderBeanFactory orderBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.orderBeanFactory = orderBeanFactory;
    }

    @Override
    protected CheckoutThankYouPageContent getViewModelInstance() {
        return new CheckoutThankYouPageContent();
    }

    @Override
    public final CheckoutThankYouPageContent create(final Order order) {
        return super.create(order);
    }

    @Override
    protected final void initialize(final CheckoutThankYouPageContent model, final Order order) {
        super.initialize(model, order);
        fillOrder(model, order);
    }

    @Override
    protected void fillTitle(final CheckoutThankYouPageContent model, final Order order) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:thankYouPage.title"));
    }

    protected void fillOrder(final CheckoutThankYouPageContent model, final Order order) {
        model.setOrder(orderBeanFactory.create(order));
    }
}
