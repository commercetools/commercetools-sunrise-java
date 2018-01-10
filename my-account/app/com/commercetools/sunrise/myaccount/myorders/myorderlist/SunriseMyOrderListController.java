package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyOrderListController extends SunriseContentController implements WithContent {

    private final MyOrderListFetcher myOrderListFinder;

    protected SunriseMyOrderListController(final ContentRenderer contentRenderer,
                                           final MyOrderListFetcher myOrderListFinder) {
        super(contentRenderer);
        this.myOrderListFinder = myOrderListFinder;
    }

    @EnableHooks
    @SunriseRoute(MyOrdersReverseRouter.MY_ORDER_LIST_PAGE)
    public CompletionStage<Result> show() {
        return myOrderListFinder.get().thenComposeAsync(orders -> {
            final PageData pageData = PageData.of()
                    .putField("orders", orders);
            return okResultWithPageContent(pageData);
        }, HttpExecution.defaultContext());
    }
}
