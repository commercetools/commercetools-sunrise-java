package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.orders.OrderFetcher;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.SunriseCheckoutThankYouController;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels.CheckoutThankYouPageContentFactory;
import controllers.productcatalog.routes;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents(CheckoutStepControllerComponent.class)
public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    @Inject
    public CheckoutThankYouController(final ContentRenderer contentRenderer,
                                      final OrderFetcher orderCreatedFinder,
                                      final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory) {
        super(contentRenderer, orderCreatedFinder, checkoutThankYouPageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleNotFoundOrderCreated() {
        return redirectAsync(routes.HomeController.show());
    }
}
