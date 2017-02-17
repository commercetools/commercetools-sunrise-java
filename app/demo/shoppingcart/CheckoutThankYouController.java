package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.OrderCreatedFinder;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.SunriseCheckoutThankYouController;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CheckoutThankYouController(final ComponentRegistry componentRegistry,
                                      final TemplateRenderer templateRenderer,
                                      final OrderCreatedFinder orderCreatedFinder,
                                      final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory,
                                      final HomeReverseRouter homeReverseRouter) {
        super(componentRegistry, templateRenderer, orderCreatedFinder, checkoutThankYouPageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Inject
    public void registerComponents(final CheckoutStepControllerComponent checkoutStepControllerComponent) {
        register(checkoutStepControllerComponent);
    }

    @Override
    public CompletionStage<Result> handleNotFoundOrderCreated() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
