package com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.OrderWithCustomer;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends PageContentFactory<MyOrderDetailPageContent, OrderWithCustomer> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    public MyOrderDetailPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    @Override
    protected MyOrderDetailPageContent newViewModelInstance(final OrderWithCustomer orderWithCustomer) {
        return new MyOrderDetailPageContent();
    }

    @Override
    public final MyOrderDetailPageContent create(final OrderWithCustomer orderWithCustomer) {
        return super.create(orderWithCustomer);
    }

    @Override
    protected final void initialize(final MyOrderDetailPageContent viewModel, final OrderWithCustomer orderWithCustomer) {
        super.initialize(viewModel, orderWithCustomer);
        fillOrder(viewModel, orderWithCustomer);
    }

    @Override
    protected void fillTitle(final MyOrderDetailPageContent viewModel, final OrderWithCustomer orderWithCustomer) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("myAccount:myOrderDetailPage.title"));
    }

    protected void fillOrder(final MyOrderDetailPageContent viewModel, final OrderWithCustomer orderWithCustomer) {
        viewModel.setOrder(orderWithCustomer.getOrder());
    }
}
