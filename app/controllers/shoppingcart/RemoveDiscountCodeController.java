package controllers.shoppingcart;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.shoppingcart.removediscountcode.RemoveDiscountCodeControllerAction;
import com.commercetools.sunrise.shoppingcart.removediscountcode.RemoveDiscountCodeFormData;
import com.commercetools.sunrise.shoppingcart.removediscountcode.SunriseRemoveDiscountCodeController;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class RemoveDiscountCodeController extends SunriseRemoveDiscountCodeController {

    @Inject
    RemoveDiscountCodeController(final ContentRenderer contentRenderer,
                                 final FormFactory formFactory,
                                 final RemoveDiscountCodeFormData formData,
                                 final CartPageContentFactory pageContentFactory,
                                 final RemoveDiscountCodeControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, pageContentFactory, controllerAction);
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
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveDiscountCodeFormData formData) {
        return redirectAsync(routes.CartContentController.show());
    }
}
