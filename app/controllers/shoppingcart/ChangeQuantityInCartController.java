package controllers.shoppingcart;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.changequantity.ChangeQuantityInCartFormAction;
import com.commercetools.sunrise.shoppingcart.changequantity.ChangeQuantityInCartFormData;
import com.commercetools.sunrise.shoppingcart.changequantity.SunriseChangeQuantityInCartController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class ChangeQuantityInCartController extends SunriseChangeQuantityInCartController {

    @Inject
    public ChangeQuantityInCartController(final ContentRenderer contentRenderer,
                                          final FormFactory formFactory,
                                          final ChangeQuantityInCartFormData formData,
                                          final ChangeQuantityInCartFormAction controllerAction) {
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
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeQuantityInCartFormData formData) {
        return redirectAsync(routes.CartContentController.show());
    }
}
