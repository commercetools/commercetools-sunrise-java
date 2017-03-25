package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels.MyOrderDetailPageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseContentController
        implements MyAccountController, WithQueryFlow<OrderWithCustomer>, WithRequiredCustomer, WithRequiredMyOrder {

    private final CustomerFinder customerFinder;
    private final MyOrderFinder myOrderFinder;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final ContentRenderer contentRenderer,
                                             final CustomerFinder customerFinder, final MyOrderFinder myOrderFinder,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(contentRenderer);
        this.customerFinder = customerFinder;
        this.myOrderFinder = myOrderFinder;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public final MyOrderFinder getMyOrderFinder() {
        return myOrderFinder;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String orderIdentifier) {
        return requireCustomer(customer ->
                requireMyOrder(customer, orderIdentifier, myOrder ->
                        showPage(OrderWithCustomer.of(myOrder, customer))));
    }

    @Override
    public PageContent createPageContent(final OrderWithCustomer orderWithCustomer) {
        return myOrderDetailPageContentFactory.create(orderWithCustomer);
    }
}
