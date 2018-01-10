package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels.MyOrderListPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class MyOrderListController extends SunriseMyOrderListController {

    @Inject
    public MyOrderListController(final ContentRenderer contentRenderer,
                                 final MyOrderListFetcher myOrderListFinder,
                                 final MyOrderListPageContentFactory pageContentFactory) {
        super(contentRenderer, myOrderListFinder, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
