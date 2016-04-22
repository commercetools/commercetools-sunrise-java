package myaccount.orders;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import myaccount.CustomerSessionUtils;
import myaccount.common.MyAccountController;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;

@Singleton
public class MyOrdersPageController extends MyAccountController {

    public static final int PAGE_SIZE = 500; // TODO allow to configure and work with pages

    @Inject
    public MyOrdersPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public CompletionStage<Result> list(final String languageTag, final int page) {
        final UserContext userContext = userContext(languageTag);
        return getCustomerOrders(session(), page).thenApplyAsync(orders -> {
            final MyOrdersPageContent pageContent = new MyOrdersPageContent(orders, userContext, i18nResolver(), reverseRouter());
            return ok(renderMyOrdersPage(pageContent, userContext));
        }, HttpExecution.defaultContext());
    }

    public CompletionStage<Result> show(final String languageTag, final String orderNumber) {
        final UserContext userContext = userContext(languageTag);
        return getCustomerOrder(session(), orderNumber).thenApplyAsync(orderOpt -> {
            final MyOrderPageContent pageContent = new MyOrderPageContent();
            return ok(renderMyOrderPage(pageContent, userContext));
        }, HttpExecution.defaultContext());
    }

    protected CompletionStage<List<Order>> getCustomerOrders(final Http.Session session, final int page) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(customerId -> fetchOrdersByCustomer(customerId, page, PAGE_SIZE))
                .orElseGet(() -> CompletableFuture.completedFuture(emptyList()));
    }

    protected CompletionStage<Optional<Order>> getCustomerOrder(final Http.Session session, final String orderNumber) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(customerId -> fetchOrderByCustomer(customerId, orderNumber))
                .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty()));
    }

    protected CompletionStage<List<Order>> fetchOrdersByCustomer(final String customerId, final int page, final int pageSize) {
        final int offset = (page - 1) * PAGE_SIZE;
        final OrderQuery query = OrderQuery.of().byCustomerId(customerId)
                .withOffset(offset)
                .withLimit(pageSize);
        return sphere().execute(query).thenApply(PagedQueryResult::getResults);
    }

    protected CompletionStage<Optional<Order>> fetchOrderByCustomer(final String customerId, final String orderNumber) {
        final OrderQuery query = OrderQuery.of().byCustomerId(customerId)
                .plusPredicates(order -> order.orderNumber().is(orderNumber))
                .withLimit(1);
        return sphere().execute(query).thenApply(PagedQueryResult::head);
    }

    protected Html renderMyOrdersPage(final MyOrdersPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("my-account-my-orders", pageData, userContext.locales());
    }

    protected Html renderMyOrderPage(final MyOrderPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("my-account-my-orders-order", pageData, userContext.locales());
    }
}
