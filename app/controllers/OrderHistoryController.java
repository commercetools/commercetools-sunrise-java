package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class OrderHistoryController extends SunriseController {

    private static final String MY_ORDERS_TEMPLATE = "my-account-my-orders";
    private static final String MY_ORDER_TEMPLATE = "my-account-my-orders-order";

    private final TemplateEngine templateEngine;
    private final MyOrderListFetcher myOrderListFinder;
    private final MyOrderFetcher myOrderFetcher;

    @Inject
    OrderHistoryController(final TemplateEngine templateEngine,
                           final MyOrderListFetcher myOrderListFinder,
                           final MyOrderFetcher myOrderFetcher) {
        this.templateEngine = templateEngine;
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderFetcher = myOrderFetcher;
    }

    @EnableHooks
    public CompletionStage<Result> list() {
        return myOrderListFinder.get()
                .thenApply(orders -> PageData.of().put("orders", orders))
                .thenComposeAsync(pageData -> templateEngine.render(MY_ORDERS_TEMPLATE, pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    public CompletionStage<Result> show(final String orderIdentifier) {
        return myOrderFetcher.get(orderIdentifier)
                .thenApply(order -> PageData.of().put("order", order))
                .thenComposeAsync(pageData -> templateEngine.render(MY_ORDER_TEMPLATE, pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }
}
