package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.MyOrdersLocalizedReverseRouter;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.orders.Order;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

public abstract class SunriseMyOrderDetailController extends SunriseFrameworkMyAccountController implements WithTemplateName {

    private final MyOrdersLocalizedReverseRouter myOrdersLocalizedReverseRouter;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;
    private final OrderFinder orderFinder;

    protected SunriseMyOrderDetailController(final MyOrdersLocalizedReverseRouter myOrdersLocalizedReverseRouter,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory,
                                             final OrderFinder orderFinder) {
        this.myOrdersLocalizedReverseRouter = myOrdersLocalizedReverseRouter;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
        this.orderFinder = orderFinder;
    }

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
        return doRequest(() -> orderFinder.findOrder(orderNumber)
                .thenComposeAsync(order -> order
                                .map(this::showOrder)
                                .orElseGet(this::handleNotFoundOrder),
                        HttpExecution.defaultContext()));
    }

    protected CompletionStage<Result> showOrder(final Order order) {
        return asyncOk(renderPage(order));
    }

    protected abstract CompletionStage<Result> handleNotFoundOrder()

    protected CompletionStage<Html> renderPage(final Order order) {
        final MyOrderDetailControllerData myOrderDetailControllerData = new MyOrderDetailControllerData(order);
        final MyOrderDetailPageContent pageContent = myOrderDetailPageContentFactory.create(myOrderDetailControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected final CompletionStage<Result> redirectToMyOrders() {
        return redirectTo(myOrdersLocalizedReverseRouter.myOrderListPageCall());
    }
}
