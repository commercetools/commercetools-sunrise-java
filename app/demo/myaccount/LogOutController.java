package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.common.sessions.customers.CustomerInSession;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import com.commercetools.sunrise.common.sessions.carts.CartInSession;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final RequestHookContext hookContext,
                            final CustomerInSession customerInSession,
                            final CartInSession cartInSession,
                            final HomeReverseRouter homeReverseRouter) {
        super(hookContext, customerInSession, cartInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleSuccessfulAction() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
