package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContent;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(MyOrderListThemeLinksControllerComponent.class)
public abstract class SunriseMyOrderListController extends SunriseFrameworkMyAccountController implements WithTemplateName, WithQueryFlow<Customer, PagedQueryResult<Order>> {

    private final MyOrderListQuery myOrderListQuery;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final CustomerFinder customerFinder, final MyOrderListQuery myOrderListQuery,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(customerFinder);
        this.myOrderListQuery = myOrderListQuery;
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
        return doRequest(() -> requireCustomer(this::showPage));
    }

    @Override
    public CompletionStage<PagedQueryResult<Order>> doQuery(final Customer customer) {
        return myOrderListQuery.apply(customer);
    }

    @Override
    public CompletionStage<Html> renderPage(final Customer customer, final PagedQueryResult<Order> orders) {
        final MyOrderListPageContent pageContent = myOrderListPageContentFactory.create(orders);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}
