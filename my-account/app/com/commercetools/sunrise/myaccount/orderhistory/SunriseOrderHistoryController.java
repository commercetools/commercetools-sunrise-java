package com.commercetools.sunrise.myaccount.orderhistory;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseOrderHistoryController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final MyOrderListFetcher myOrderListFinder;
    private final MyOrderFetcher myOrderFetcher;

    protected SunriseOrderHistoryController(final TemplateEngine templateEngine,
                                            final MyOrderListFetcher myOrderListFinder,
                                            final MyOrderFetcher myOrderFetcher) {
        this.templateEngine = templateEngine;
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderFetcher = myOrderFetcher;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_LIST_PAGE)
    public CompletionStage<Result> list() {
        return myOrderListFinder.get()
                .thenApply(orders -> PageData.of().put("orders", orders))
                .thenComposeAsync(pageData -> templateEngine.render("my-account-my-orders", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String orderIdentifier) {
        return myOrderFetcher.get(orderIdentifier)
                .thenApply(order -> PageData.of().put("order", order))
                .thenComposeAsync(pageData -> templateEngine.render("my-account-my-orders-order", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }
}
