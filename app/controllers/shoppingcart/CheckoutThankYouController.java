package controllers.shoppingcart;

import com.commercetools.sunrise.framework.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.framework.checkout.thankyou.OrderCreatedFinder;
import com.commercetools.sunrise.framework.checkout.thankyou.SunriseCheckoutThankYouController;
import com.commercetools.sunrise.framework.checkout.thankyou.viewmodels.CheckoutThankYouPageContentFactory;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        CheckoutStepControllerComponent.class
})
public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public CheckoutThankYouController(final ContentRenderer contentRenderer,
                                      final OrderCreatedFinder orderCreatedFinder,
                                      final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory,
                                      final HomeReverseRouter homeReverseRouter) {
        super(contentRenderer, orderCreatedFinder, checkoutThankYouPageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public CompletionStage<Result> handleNotFoundOrderCreated() {
        return redirectToCall(homeReverseRouter.homePageCall());
    }
}
