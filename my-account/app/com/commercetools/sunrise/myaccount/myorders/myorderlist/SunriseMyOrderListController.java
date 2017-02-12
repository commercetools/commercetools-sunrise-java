package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContent;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(MyOrderListThemeLinksControllerComponent.class)
public abstract class SunriseMyOrderListController extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFetchFlow<OrderListWithCustomer> {

    private final MyOrderListFinder myOrderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final CustomerFinder customerFinder, final MyOrderListFinder myOrderListFinder,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(customerFinder);
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderListPageContentFactory = myOrderListPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("my-orders", "my-order-list", "order"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders";
    }

    @SunriseRoute("myOrderListPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireOrderListWithCustomer(this::showPage));
    }

    @Override
    public CompletionStage<Content> renderPage(final OrderListWithCustomer orderListWithCustomer) {
        final MyOrderListPageContent pageContent = myOrderListPageContentFactory.create(orderListWithCustomer);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    protected final CompletionStage<Result> requireOrderListWithCustomer(final Function<OrderListWithCustomer, CompletionStage<Result>> nextAction) {
        return requireCustomer(customer -> myOrderListFinder.apply(customer)
                .thenApply(orders -> OrderListWithCustomer.of(orders, customer))
                .thenComposeAsync(nextAction, HttpExecution.defaultContext()));
    }
}
