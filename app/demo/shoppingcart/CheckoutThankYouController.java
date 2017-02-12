package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.OrderFinder;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.SunriseCheckoutThankYouController;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CheckoutThankYouController(final OrderFinder orderFinder,
                                      final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory,
                                      final HomeReverseRouter homeReverseRouter) {
        super(orderFinder, checkoutThankYouPageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundOrder() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
