package com.commercetools.sunrise.myaccount.myorders.myorderlist;

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
import com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels.MyOrderListPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseMyOrderListController extends SunriseContentController
        implements MyAccountController, WithQueryFlow<OrderListWithCustomer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final MyOrderListFinder myOrderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final ContentRenderer contentRenderer,
                                           final CustomerFinder customerFinder, final MyOrderListFinder myOrderListFinder,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(contentRenderer);
        this.customerFinder = customerFinder;
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderListPageContentFactory = myOrderListPageContentFactory;
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_LIST_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(customer ->
                findMyOrderList(customer, orders ->
                        showPage(OrderListWithCustomer.of(orders, customer))));
    }

    @Override
    public PageContent createPageContent(final OrderListWithCustomer orderListWithCustomer) {
        return myOrderListPageContentFactory.create(orderListWithCustomer);
    }

    protected final CompletionStage<Result> findMyOrderList(final Customer customer, final Function<PagedQueryResult<Order>, CompletionStage<Result>> nextAction) {
        return myOrderListFinder.apply(customer)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }
}
