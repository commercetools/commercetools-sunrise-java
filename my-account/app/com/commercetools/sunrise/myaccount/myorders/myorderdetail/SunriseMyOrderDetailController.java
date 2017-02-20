package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyOrdersReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels.MyOrderDetailPageContentFactory;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseTemplateFormController
        implements MyAccountController, WithQueryFlow<OrderWithCustomer>, WithRequiredCustomer, WithRequiredMyOrder {

    private final CustomerFinder customerFinder;
    private final MyOrderFinder myOrderFinder;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                             final CustomerFinder customerFinder, final MyOrderFinder myOrderFinder,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myOrderFinder = myOrderFinder;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public MyOrderFinder getMyOrderFinder() {
        return myOrderFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String orderIdentifier) {
        return requireCustomer(customer ->
                requireMyOrder(customer, orderIdentifier, myOrder ->
                        showPage(OrderWithCustomer.of(myOrder, customer))));
    }

    @Override
    public PageContent createPageContent(final OrderWithCustomer orderWithCustomer) {
        return myOrderDetailPageContentFactory.create(orderWithCustomer);
    }
}
