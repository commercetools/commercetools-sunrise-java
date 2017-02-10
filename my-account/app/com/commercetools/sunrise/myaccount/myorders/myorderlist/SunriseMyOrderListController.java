package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(MyOrderListThemeLinksControllerComponent.class)
public abstract class SunriseMyOrderListController extends SunriseFrameworkMyAccountController implements WithTemplateName {

    private final OrderListFinder orderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final OrderListFinder orderListFinder, final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        this.orderListFinder = orderListFinder;
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
        return doRequest(() -> orderListFinder.findOrders()
                .thenComposeAsync(this::showOrders, HttpExecution.defaultContext()));
    }

    protected CompletionStage<Result> showOrders(final PagedQueryResult<Order> orders) {
        return asyncOk(renderPage(orders));
    }

    protected CompletionStage<Html> renderPage(final PagedQueryResult<Order> orderQueryResult) {
        final MyOrderListControllerData myOrderListControllerData = new MyOrderListControllerData(orderQueryResult);
        final MyOrderListPageContent pageContent = myOrderListPageContentFactory.create(myOrderListControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}
