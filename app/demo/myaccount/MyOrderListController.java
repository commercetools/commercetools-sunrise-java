package demo.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.MyOrderListPageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.OrderListFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RequestScoped
public class MyOrderListController extends SunriseMyOrderListController {

    private final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter;

    @Inject
    public MyOrderListController(final OrderListFinder orderListFinder, final MyOrderListPageContentFactory myOrderListPageContentFactory,
                                 final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter) {
        super(orderListFinder, myOrderListPageContentFactory);
        this.authenticationLocalizedReverseRouter = authenticationLocalizedReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationLocalizedReverseRouter.showLogInForm());
    }
}
