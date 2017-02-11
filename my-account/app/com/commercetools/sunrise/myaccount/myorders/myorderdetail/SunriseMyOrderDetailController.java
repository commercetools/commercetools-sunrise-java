package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.orders.Order;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

public abstract class SunriseMyOrderDetailController extends SunriseFrameworkMyAccountController implements WithTemplateName {

    private final MyOrderFinder myOrderFinder;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final CustomerFinder customerFinder, final MyOrderFinder myOrderFinder,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(customerFinder);
        this.myOrderFinder = myOrderFinder;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
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
        return doRequest(() ->
                requireCustomer(customer ->
                        myOrderFinder.findOrder(customer, orderNumber)
                                .thenComposeAsync(order -> order
                                                .map(this::showOrder)
                                                .orElseGet(this::handleNotFoundOrder),
                                        HttpExecution.defaultContext())));
    }

    protected CompletionStage<Result> showOrder(final Order order) {
        return asyncOk(renderPage(order));
    }

    protected CompletionStage<Html> renderPage(final Order order) {
        final MyOrderDetailControllerData myOrderDetailControllerData = new MyOrderDetailControllerData(order);
        final MyOrderDetailPageContent pageContent = myOrderDetailPageContentFactory.create(myOrderDetailControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected abstract CompletionStage<Result> handleNotFoundOrder();
}
