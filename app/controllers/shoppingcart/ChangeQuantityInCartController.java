package controllers.shoppingcart;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.changequantity.ChangeQuantityInCartControllerAction;
import com.commercetools.sunrise.shoppingcart.changequantity.ChangeQuantityInCartFormData;
import com.commercetools.sunrise.shoppingcart.changequantity.SunriseChangeQuantityInCartController;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class,
        MiniWishlistControllerComponent.class
})
public final class ChangeQuantityInCartController extends SunriseChangeQuantityInCartController {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public ChangeQuantityInCartController(final ContentRenderer contentRenderer,
                                          final FormFactory formFactory,
                                          final ChangeQuantityInCartFormData formData,
                                          final CartFinder cartFinder,
                                          final CartPageContentFactory pageContentFactory,
                                          final ChangeQuantityInCartControllerAction controllerAction,
                                          final CartReverseRouter cartReverseRouter) {
        super(contentRenderer, formFactory, formData, cartFinder, pageContentFactory, controllerAction);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeQuantityInCartFormData formData) {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }
}
