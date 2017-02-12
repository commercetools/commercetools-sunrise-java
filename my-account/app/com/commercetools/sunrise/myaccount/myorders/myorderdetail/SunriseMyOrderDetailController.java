package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.view.MyOrderDetailPageContent;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.view.MyOrderDetailPageContentFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

public abstract class SunriseMyOrderDetailController extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFetchFlow<OrderWithCustomer> {

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
        return doRequest(() -> requireOrder(orderNumber, this::showPage));
    }

    @Override
    public CompletionStage<Content> renderPage(final OrderWithCustomer orderWithCustomer) {
        final MyOrderDetailPageContent pageContent = myOrderDetailPageContentFactory.create(orderWithCustomer);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected final CompletionStage<Result> requireOrder(final String orderNumber,
                                                         final Function<OrderWithCustomer, CompletionStage<Result>> nextAction) {
        return requireCustomer(customer ->
                myOrderFinder.apply(customer, orderNumber)
                        .thenComposeAsync(orderOpt -> orderOpt
                                        .map(order -> OrderWithCustomer.of(order, customer))
                                        .map(nextAction)
                                        .orElseGet(this::handleNotFoundOrder),
                                HttpExecution.defaultContext()));
    }

    protected abstract CompletionStage<Result> handleNotFoundOrder();
}
