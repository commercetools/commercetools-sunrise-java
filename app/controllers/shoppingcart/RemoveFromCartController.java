package controllers.shoppingcart;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.remove.RemoveFromCartControllerAction;
import com.commercetools.sunrise.shoppingcart.remove.RemoveFromCartFormData;
import com.commercetools.sunrise.shoppingcart.remove.SunriseRemoveFromCartController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class RemoveFromCartController extends SunriseRemoveFromCartController {

    @Inject
    public RemoveFromCartController(final ContentRenderer contentRenderer,
                                    final FormFactory formFactory,
                                    final RemoveFromCartFormData formData,
                                    final RemoveFromCartControllerAction removeFromCartControllerAction) {
        super(contentRenderer, formFactory, formData, removeFromCartControllerAction);
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
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveFromCartFormData formData) {
        return redirectAsync(routes.CartContentController.show());
    }
}
