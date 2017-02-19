package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyOrdersReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseMyOrderListController extends SunriseTemplateFormController implements WithQueryFlow<OrderListWithCustomer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final MyOrderListFinder myOrderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                           final CustomerFinder customerFinder, final MyOrderListFinder myOrderListFinder,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderListPageContentFactory = myOrderListPageContentFactory;
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @RunRequestStartedHook
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
