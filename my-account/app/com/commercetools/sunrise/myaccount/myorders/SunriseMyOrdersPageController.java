package com.commercetools.sunrise.myaccount.myorders;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.models.ProductDataConfig;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import com.commercetools.sunrise.myaccount.CustomerSessionUtils;
import com.commercetools.sunrise.myaccount.common.MyAccountController;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import com.commercetools.sunrise.shoppingcart.CartLikeBean;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public abstract class SunriseMyOrdersPageController extends MyAccountController {

    public static final int PAGE_SIZE = 500; // TODO allow to configure and work with pages
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    protected ReverseRouter reverseRouter;
    @Inject
    protected I18nResolver i18nResolver;

    public CompletionStage<Result> list(final String languageTag, final int page) {
        return getCustomerOrders(page)
                .thenApplyAsync(orders -> {
                    final MyOrdersPageContent pageContent = createMyOrdersPage(orders, userContext());
                    return ok(renderMyOrdersPage(pageContent, userContext()));
                }, HttpExecution.defaultContext());
    }

    public CompletionStage<Result> show(final String languageTag, final String orderNumber) {
        return getCustomerOrder(session(), orderNumber)
                .thenComposeAsync(orderOpt -> orderOpt
                        .map(order -> handleFoundOrder(order, userContext()))
                        .orElseGet(() -> handleNotFoundOrder(userContext())),
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<List<Order>> getCustomerOrders(final int page) {
        return CustomerSessionUtils.getCustomerId(session())
                .map(customerId -> fetchOrdersByCustomer(customerId, page, PAGE_SIZE))
                .orElseGet(() -> completedFuture(emptyList()));
    }

    protected CompletionStage<Optional<Order>> getCustomerOrder(final Http.Session session, final String orderNumber) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(customerId -> fetchOrderByCustomer(customerId, orderNumber))
                .orElseGet(() -> completedFuture(Optional.empty()));
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
                .plusExpansionPaths(order -> order.paymentInfo().payments())
                .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                .withLimit(1);
        return sphere().execute(query).thenApply(PagedQueryResult::head);
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order, final UserContext userContext) {
        final MyOrderPageContent pageContent = createMyOrderPage(order, userContext);
        return completedFuture(ok(renderMyOrderPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleNotFoundOrder(final UserContext userContext) {
        final Call call = reverseRouter.showMyOrders(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected MyOrderPageContent createMyOrderPage(final Order order, final UserContext userContext) {
        final MyOrderPageContent pageContent = new MyOrderPageContent();
        pageContent.setOrder(new CartLikeBean(order, userContext, productDataConfig, reverseRouter));
        return pageContent;
    }

    protected MyOrdersPageContent createMyOrdersPage(final List<Order> orders, final UserContext userContext) {
        final MyOrdersPageContent pageContent = new MyOrdersPageContent();
        final List<OrderOverviewBean> orderBeans = orders.stream()
                .map(order -> new OrderOverviewBean(order, userContext, i18nResolver, reverseRouter))
                .collect(toList());
        pageContent.setOrder(orderBeans);
        return pageContent;
    }

    //TODO split this controller into two
    protected Html renderMyOrdersPage(final MyOrdersPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(pageContent);
        return templateEngine().renderToHtml("my-account-my-orders", pageData, userContext.locales());
    }

    protected Html renderMyOrderPage(final MyOrderPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(pageContent);
        return templateEngine().renderToHtml("my-account-my-orders-order", pageData, userContext.locales());
    }
}
