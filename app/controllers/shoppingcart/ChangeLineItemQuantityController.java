package controllers.shoppingcart;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.framework.cart.changelineitemquantity.ChangeLineItemQuantityControllerAction;
import com.commercetools.sunrise.framework.cart.changelineitemquantity.ChangeLineItemQuantityFormData;
import com.commercetools.sunrise.framework.cart.changelineitemquantity.SunriseChangeLineItemQuantityController;
import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class ChangeLineItemQuantityController extends SunriseChangeLineItemQuantityController {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public ChangeLineItemQuantityController(final ContentRenderer contentRenderer,
                                            final FormFactory formFactory,
                                            final ChangeLineItemQuantityFormData formData,
                                            final CartFinder cartFinder,
                                            final CartDetailPageContentFactory pageContentFactory,
                                            final ChangeLineItemQuantityControllerAction controllerAction,
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
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final ChangeLineItemQuantityFormData formData) {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }
}
