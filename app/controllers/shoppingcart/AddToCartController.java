package controllers.shoppingcart;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.add.AddToCartControllerAction;
import com.commercetools.sunrise.shoppingcart.add.AddToCartFormData;
import com.commercetools.sunrise.shoppingcart.add.SunriseAddToCartController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class AddToCartController extends SunriseAddToCartController {

    @Inject
    public AddToCartController(final ContentRenderer contentRenderer,
                               final FormFactory formFactory,
                               final AddToCartFormData formData,
                               final AddToCartControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddToCartFormData formData) {
        return redirectAsync(routes.CartContentController.show());
    }
}
