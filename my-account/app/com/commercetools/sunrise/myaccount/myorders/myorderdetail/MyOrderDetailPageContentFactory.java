package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.OrderBeanFactory;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends PageContentFactory<MyOrderDetailPageContent, MyOrderDetailControllerData> {

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
    public final MyOrderDetailPageContent create(final MyOrderDetailControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final MyOrderDetailPageContent model, final MyOrderDetailControllerData data) {
        super.initialize(model, data);
        fillOrder(model, data);
    }

    @Override
    protected void fillTitle(final MyOrderDetailPageContent model, final MyOrderDetailControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:myOrderDetailPage.title"));
    }

    protected void fillOrder(final MyOrderDetailPageContent model, final MyOrderDetailControllerData data) {
        model.setOrder(orderBeanFactory.create(data.getOrder()));
    }
}
