package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.sessions.cart.CartInSession;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final ComponentRegistry componentRegistry,
                            final CustomerInSession customerInSession,
                            final CartInSession cartInSession,
                            final HomeReverseRouter homeReverseRouter) {
        super(componentRegistry, customerInSession, cartInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleSuccessfulAction() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
