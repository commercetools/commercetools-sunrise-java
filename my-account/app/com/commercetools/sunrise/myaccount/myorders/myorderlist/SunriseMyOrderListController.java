package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(MyOrderListThemeLinksControllerComponent.class)
public abstract class SunriseMyOrderListController extends SunriseTemplateFormController implements WithQueryFlow<OrderListWithCustomer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final MyOrderListFinder myOrderListFinder;
    private final MyOrderListPageContentFactory myOrderListPageContentFactory;

    protected SunriseMyOrderListController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                           final FormFactory formFactory, final CustomerFinder customerFinder, final MyOrderListFinder myOrderListFinder,
                                           final MyOrderListPageContentFactory myOrderListPageContentFactory) {
        super(hookContext, templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myOrderListFinder = myOrderListFinder;
        this.myOrderListPageContentFactory = myOrderListPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("my-orders", "my-order-list", "order"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @SunriseRoute("myOrderListPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() ->
                requireCustomer(customer ->
                        findMyOrderList(customer, orders ->
                                showPage(OrderListWithCustomer.of(orders, customer)))));
    }

    @Override
    public PageContent createPageContent(final OrderListWithCustomer orderListWithCustomer) {
        return myOrderListPageContentFactory.create(orderListWithCustomer);
    }

    protected final CompletionStage<Result> findMyOrderList(final Customer customer, final Function<PagedQueryResult<Order>, CompletionStage<Result>> nextAction) {
        return myOrderListFinder.apply(customer)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }
}
