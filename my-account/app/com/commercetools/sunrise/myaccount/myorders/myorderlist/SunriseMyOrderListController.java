package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels.MyOrderListPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseMyOrderListController extends SunriseContentController
        implements MyAccountController, WithQueryFlow<OrderListWithCustomer> {

    private final MyOrderListFetcher myOrderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final ContentRenderer contentRenderer,
                                           final MyOrderListFetcher myOrderListFinder,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(contentRenderer);
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderListPageContentFactory = myOrderListPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_LIST_PAGE)
    public CompletionStage<Result> show() {
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
