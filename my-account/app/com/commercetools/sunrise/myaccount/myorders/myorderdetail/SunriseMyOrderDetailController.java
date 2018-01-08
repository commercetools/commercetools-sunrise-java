package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.customers.MyCustomerFetcher;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels.MyOrderDetailPageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseContentController implements MyAccountController, WithQueryFlow<String> {

    private final MyOrderFetcher myOrderFetcher;
    private final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory;

    protected SunriseMyOrderDetailController(final ContentRenderer contentRenderer,
                                             final MyOrderFetcher myOrderFetcher,
                                             final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory) {
        super(contentRenderer);
        this.myOrderFetcher = myOrderFetcher;
        this.myOrderDetailPageContentFactory = myOrderDetailPageContentFactory;
    }

    public final MyOrderFetcher getMyOrderFetcher() {
        return myOrderFetcher;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String orderIdentifier) {
        return myOrderFetcher.apply(orderIdentifier).thenApply(orderOpt -> showPage())
    }

    @Override
    public PageContent createPageContent(final String orderIdentifier) {
        return myOrderDetailPageContentFactory.create(orderWithCustomer);
    }
}
