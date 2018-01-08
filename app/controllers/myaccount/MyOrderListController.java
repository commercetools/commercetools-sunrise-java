package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.models.customers.MyCustomerFetcher;
import com.commercetools.sunrise.models.orders.MyOrderListFetcher;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels.MyOrderListPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class MyOrderListController extends SunriseMyOrderListController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyOrderListController(final ContentRenderer contentRenderer,
                                 final MyCustomerFetcher customerFinder,
                                 final MyOrderListFetcher myOrderListFinder,
                                 final MyOrderListPageContentFactory pageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(contentRenderer, customerFinder, myOrderListFinder, pageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectToCall(authenticationReverseRouter.logInPageCall());
    }
}
