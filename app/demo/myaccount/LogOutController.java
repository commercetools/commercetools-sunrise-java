package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import com.commercetools.sunrise.myaccount.authentication.logout.SunriseLogOutController;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class LogOutController extends SunriseLogOutController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public LogOutController(final CustomerInSession customerInSession, final CartInSession cartInSession, final HomeReverseRouter homeReverseRouter) {
        super(customerInSession, cartInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleSuccessfulAction() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
