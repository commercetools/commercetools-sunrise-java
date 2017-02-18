package controllers.myaccount;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final CustomerInSession customerInSession,
                            final CartInSession cartInSession,
                            final HomeReverseRouter homeReverseRouter) {
        super(customerInSession, cartInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output) {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
