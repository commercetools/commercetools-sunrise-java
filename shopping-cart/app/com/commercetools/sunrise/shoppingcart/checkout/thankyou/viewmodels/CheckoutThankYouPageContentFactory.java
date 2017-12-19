package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class CheckoutThankYouPageContentFactory extends PageContentFactory<CheckoutThankYouPageContent, Order> {

    @Inject
    public CheckoutThankYouPageContentFactory() {
    }

    @Override
    protected CheckoutThankYouPageContent newViewModelInstance(final Order order) {
        return new CheckoutThankYouPageContent();
    }

    @Override
    public final CheckoutThankYouPageContent create(final Order order) {
        return super.create(order);
    }

    @Override
    protected final void initialize(final CheckoutThankYouPageContent viewModel, final Order order) {
        super.initialize(viewModel, order);
        fillOrder(viewModel, order);
    }

    protected void fillOrder(final CheckoutThankYouPageContent viewModel, final Order order) {
        viewModel.setOrder(order);
    }
}
