package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.OrderBeanFactory;

import javax.inject.Inject;

@RequestScoped
public class CheckoutThankYouPageContentFactory extends PageContentFactory<CheckoutThankYouPageContent, CheckoutThankYouPageData> {

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
    public final CheckoutThankYouPageContent create(final CheckoutThankYouPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutThankYouPageContent model, final CheckoutThankYouPageData data) {
        super.initialize(model, data);
        fillOrder(model, data);
    }

    @Override
    protected void fillTitle(final CheckoutThankYouPageContent model, final CheckoutThankYouPageData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:thankYouPage.title"));
    }

    protected void fillOrder(final CheckoutThankYouPageContent model, final CheckoutThankYouPageData data) {
        model.setOrder(orderBeanFactory.create(data.order));
    }
}
