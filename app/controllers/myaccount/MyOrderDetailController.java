package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.SunriseMyOrderDetailController;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class MyOrderDetailController extends SunriseMyOrderDetailController {

    @Inject
    public MyOrderDetailController(final ContentRenderer contentRenderer,
                                   final MyOrderFetcher myOrderFinder) {
        super(contentRenderer, myOrderFinder);
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders-order";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
