package com.commercetools.sunrise.myaccount.myorders.myorderdetail.view;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.OrderBeanFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends PageContentFactory<MyOrderDetailPageContent, Order> {

    private final PageTitleResolver pageTitleResolver;
    private final OrderBeanFactory orderBeanFactory;

    @Inject
    public MyOrderDetailPageContentFactory(final PageTitleResolver pageTitleResolver, final OrderBeanFactory orderBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.orderBeanFactory = orderBeanFactory;
    }

    @Override
    protected MyOrderDetailPageContent getViewModelInstance() {
        return new MyOrderDetailPageContent();
    }

    @Override
    public final MyOrderDetailPageContent create(final Order order) {
        return super.create(order);
    }

    @Override
    protected final void initialize(final MyOrderDetailPageContent model, final Order order) {
        super.initialize(model, order);
        fillOrder(model, order);
    }

    @Override
    protected void fillTitle(final MyOrderDetailPageContent model, final Order order) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:myOrderDetailPage.title"));
    }

    protected void fillOrder(final MyOrderDetailPageContent model, final Order order) {
        model.setOrder(orderBeanFactory.create(order));
    }
}
