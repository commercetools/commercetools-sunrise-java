package controllers.shoppingcart;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartDiscountCodesExpansionControllerComponent;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.shoppingcart.removediscountcode.RemoveDiscountCodeControllerAction;
import com.commercetools.sunrise.shoppingcart.removediscountcode.RemoveDiscountCodeFormData;
import com.commercetools.sunrise.shoppingcart.removediscountcode.SunriseRemoveDiscountCodeController;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class,
        CartDiscountCodesExpansionControllerComponent.class,
        MiniWishlistControllerComponent.class
})
public final class RemoveDiscountCodeController extends SunriseRemoveDiscountCodeController {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    RemoveDiscountCodeController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                        final RemoveDiscountCodeFormData formData, final CartFinder cartFinder,
                                        final CartPageContentFactory pageContentFactory, final RemoveDiscountCodeControllerAction controllerAction,
                                        final CartReverseRouter cartReverseRouter) {
        super(contentRenderer, formFactory, formData, cartFinder, pageContentFactory, controllerAction);
        this.cartReverseRouter = cartReverseRouter;
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
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveDiscountCodeFormData formData) {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }
}
