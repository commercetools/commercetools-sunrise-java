package demo.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.MyOrdersLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.myorders.myorderdetail.SunriseMyOrderDetailController;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@RequestScoped
public class MyOrderDetailController extends SunriseMyOrderDetailController {

    private final MyOrdersLocalizedReverseRouter myOrdersLocalizedReverseRouter;

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return ;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundOrder() {
        return redirectTo(myOrdersLocalizedReverseRouter.myOrderListPageCall());
    }
}
