package demo.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.MyOrderDetailPageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.MyOrderFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.SunriseMyOrderDetailController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RequestScoped
public class MyOrderDetailController extends SunriseMyOrderDetailController {

    private final MyOrdersReverseRouter myOrdersReverseRouter;
    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyOrderDetailController(final MyOrderFinder myOrderFinder,
                                   final MyOrderDetailPageContentFactory myOrderDetailPageContentFactory,
                                   final MyOrdersReverseRouter myOrdersReverseRouter,
                                   final AuthenticationReverseRouter authenticationReverseRouter) {
        super(myOrderFinder, myOrderDetailPageContentFactory);
        this.myOrdersReverseRouter = myOrdersReverseRouter;
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundOrder() {
        return redirectTo(myOrdersReverseRouter.myOrderListPageCall());
    }
}
