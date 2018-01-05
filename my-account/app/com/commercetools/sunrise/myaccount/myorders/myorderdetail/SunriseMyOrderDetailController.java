package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.models.customers.CustomerFetcher;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels.MyOrderDetailPageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseContentController
        implements MyAccountController, WithQueryFlow<OrderWithCustomer>, WithRequiredCustomer, WithRequiredMyOrder {

    private final CustomerFetcher customerFinder;
    private final MyOrderFetcher myOrderFinder;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final ContentRenderer contentRenderer,
                                             final CustomerFetcher customerFinder, final MyOrderFetcher myOrderFinder,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(contentRenderer);
        this.customerFinder = customerFinder;
        this.myOrderFinder = myOrderFinder;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
    }

    @Override
    public final CustomerFetcher getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public final MyOrderFetcher getMyOrderFinder() {
        return myOrderFinder;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String orderIdentifier) {
        return requireCustomer(customer ->
                requireMyOrder(customer, orderIdentifier, myOrder ->
                        showPage(OrderWithCustomer.of(myOrder, customer))));
    }

    @Override
    public PageContent createPageContent(final OrderWithCustomer orderWithCustomer) {
        return myOrderDetailPageContentFactory.create(orderWithCustomer);
    }
}
