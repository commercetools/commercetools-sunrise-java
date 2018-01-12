package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderDetailController extends SunriseContentController implements WithContent {

    private final MyOrderFetcher myOrderFetcher;

    protected SunriseMyOrderDetailController(final ContentRenderer contentRenderer,
                                             final MyOrderFetcher myOrderFetcher) {
        super(contentRenderer);
        this.myOrderFetcher = myOrderFetcher;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_DETAIL_PAGE)
    public CompletionStage<Result> show(final String orderIdentifier) {
        return myOrderFetcher.get(orderIdentifier).thenComposeAsync(order -> {
            final PageData pageData = PageData.of()
                    .putField("order", order);
            return okResult(pageData);
        }, HttpExecution.defaultContext());
    }
}
