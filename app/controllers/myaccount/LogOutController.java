package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.logout.LogOutControllerAction;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final LogOutControllerAction logOutControllerAction,
                            final HomeReverseRouter homeReverseRouter) {
        super(logOutControllerAction);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output) {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
