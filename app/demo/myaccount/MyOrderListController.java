package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.MyOrderListFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class MyOrderListController extends SunriseMyOrderListController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyOrderListController(final CustomerFinder customerFinder,
                                 final MyOrderListFinder myOrderListFinder,
                                 final MyOrderListPageContentFactory myOrderListPageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(customerFinder, myOrderListFinder, myOrderListPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
