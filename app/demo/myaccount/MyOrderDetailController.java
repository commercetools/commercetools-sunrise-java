package demo.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
import com.commercetools.sunrise.common.reverserouter.MyOrdersLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.MyOrderDetailPageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.OrderFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.SunriseMyOrderDetailController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RequestScoped
public class MyOrderDetailController extends SunriseMyOrderDetailController {

    private final MyOrdersLocalizedReverseRouter myOrdersLocalizedReverseRouter;
    private final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter;

    @Inject
    public MyOrderDetailController(final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory, final OrderFinder orderFinder,
                                   final MyOrdersLocalizedReverseRouter myOrdersLocalizedReverseRouter,
                                   final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter) {
        super(myOrderDetailPageContentFactory, orderFinder);
        this.myOrdersLocalizedReverseRouter = myOrdersLocalizedReverseRouter;
        this.authenticationLocalizedReverseRouter = authenticationLocalizedReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationLocalizedReverseRouter.showLogInForm());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundOrder() {
        return redirectTo(myOrdersLocalizedReverseRouter.myOrderListPageCall());
    }
}
