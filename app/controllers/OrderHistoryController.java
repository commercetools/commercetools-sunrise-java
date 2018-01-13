package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.orders.MyOrderFetcher;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import com.commercetools.sunrise.myaccount.orderhistory.SunriseOrderHistoryController;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class OrderHistoryController extends SunriseOrderHistoryController {

    @Inject
    OrderHistoryController(final TemplateEngine templateEngine,
                           final MyOrderListFetcher myOrderListFinder,
                           final MyOrderFetcher myOrderFetcher) {
        super(templateEngine, myOrderListFinder, myOrderFetcher);
    }
}
