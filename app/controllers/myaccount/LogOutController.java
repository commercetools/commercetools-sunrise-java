package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.myaccount.authentication.logout.LogOutControllerAction;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import controllers.productcatalog.routes;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class LogOutController extends SunriseLogOutController {

    @Inject
    public LogOutController(final LogOutControllerAction controllerAction) {
        super(controllerAction);
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output) {
        return redirectAsync(routes.HomeController.show());
    }
}
