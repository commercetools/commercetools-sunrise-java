package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.core.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class CheckoutThankYouPageContentFactory extends PageContentFactory<CheckoutThankYouPageContent, Order> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    public CheckoutThankYouPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
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

    @Override
    protected void fillTitle(final CheckoutThankYouPageContent viewModel, final Order order) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:thankYouPage.title"));
    }

    protected void fillOrder(final CheckoutThankYouPageContent viewModel, final Order order) {
        viewModel.setOrder(order);
    }
}
