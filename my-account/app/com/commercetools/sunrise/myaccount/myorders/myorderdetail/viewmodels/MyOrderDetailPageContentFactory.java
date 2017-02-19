package com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.OrderWithCustomer;
import com.commercetools.sunrise.common.models.carts.OrderBeanFactory;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends PageContentFactory<MyOrderDetailPageContent, OrderWithCustomer> {

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
    public final MyOrderDetailPageContent create(final OrderWithCustomer orderWithCustomer) {
        return super.create(orderWithCustomer);
    }

    @Override
    protected final void initialize(final MyOrderDetailPageContent model, final OrderWithCustomer orderWithCustomer) {
        super.initialize(model, orderWithCustomer);
        fillOrder(model, orderWithCustomer);
    }

    @Override
    protected void fillTitle(final MyOrderDetailPageContent model, final OrderWithCustomer orderWithCustomer) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:myOrderDetailPage.title"));
    }

    protected void fillOrder(final MyOrderDetailPageContent model, final OrderWithCustomer orderWithCustomer) {
        model.setOrder(orderBeanFactory.create(orderWithCustomer.getOrder()));
    }
}
