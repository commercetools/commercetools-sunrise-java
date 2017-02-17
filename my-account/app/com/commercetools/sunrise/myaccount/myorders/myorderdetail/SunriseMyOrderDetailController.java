package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.view.MyOrderDetailPageContentFactory;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseTemplateFormController implements WithQueryFlow<OrderWithCustomer>, WithRequiredCustomer, WithRequiredMyOrder {

    private final CustomerFinder customerFinder;
    private final MyOrderFinder myOrderFinder;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                             final FormFactory formFactory, final CustomerFinder customerFinder, final MyOrderFinder myOrderFinder,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(componentRegistry, templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myOrderFinder = myOrderFinder;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders-order";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public MyOrderFinder getMyOrderFinder() {
        return myOrderFinder;
    }

    @SunriseRoute("myOrderDetailPageCall")
    public CompletionStage<Result> showByOrderNumber(final String languageTag, final String orderNumber) {
        return requireCustomer(customer ->
                requireMyOrder(customer, orderNumber, myOrder ->
                        showPage(OrderWithCustomer.of(myOrder, customer))));
    }

    @Override
    public PageContent createPageContent(final OrderWithCustomer orderWithCustomer) {
        return myOrderDetailPageContentFactory.create(orderWithCustomer);
    }
}
