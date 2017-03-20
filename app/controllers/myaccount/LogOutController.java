package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.logout.LogOutControllerAction;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final LogOutControllerAction controllerAction,
                            final HomeReverseRouter homeReverseRouter) {
        super(controllerAction);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }
}
