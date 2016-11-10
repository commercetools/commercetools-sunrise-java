package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.orders.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseMyOrderDetailController extends SunriseFrameworkMyAccountController implements WithTemplateName {

    private static final Logger logger = LoggerFactory.getLogger(SunriseMyOrderDetailController.class);

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("my-orders", "my-order-detail", "order"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders-order";
    }

    @SunriseRoute("myOrderDetailPageCall")
    public CompletionStage<Result> showByOrderNumber(final String languageTag, final String orderNumber) {
        return doRequest(() -> {
            logger.debug("show order with order number={} in locale={}", orderNumber, languageTag);
            return requireOrder(orderNumber)
                    .thenComposeAsync(this::showOrder, HttpExecution.defaultContext());
        });
    }

    protected CompletionStage<Result> showOrder(final Order order) {
        return asyncOk(renderPage(order));
    }

    protected CompletionStage<Result> handleNotFoundOrder() {
        return redirectToMyOrders();
    }

    protected CompletionStage<Html> renderPage(final Order order) {
        final MyOrderDetailPageContent pageContent = injector().getInstance(MyOrderDetailPageContentFactory.class).create(order);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected CompletionStage<Order> requireOrder(final String orderNumber) {
        final String customerId = requireExistingCustomerId();
        final CustomerIdOrderNumberPair customerIdOrderNumberPair = new CustomerIdOrderNumberPair(customerId, orderNumber);
        return injector().getInstance(OrderFinderByCustomerIdAndOrderNumber.class).findOrder(customerIdOrderNumberPair)
                .thenApplyAsync(orderOpt -> {
                    final Order order = orderOpt.orElseThrow(OrderNotFoundException::new);
                    OrderLoadedHook.runHook(hooks(), order);
                    return order;
                }, HttpExecution.defaultContext());
    }

    protected final CompletionStage<Result> redirectToMyOrders() {
        final Call call = injector().getInstance(MyOrdersReverseRouter.class).myOrderListPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Inject
    private void postInit() {
        //just prepend another error handler if this does not suffice
        prependErrorHandler(e -> e instanceof OrderNotFoundException, e -> {
            logger.error("Order not found", e);
            return handleNotFoundOrder();
        });
    }
}
