package controllers.shoppingcart;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.framework.cart.removelineitem.RemoveLineItemControllerAction;
import com.commercetools.sunrise.framework.cart.removelineitem.RemoveLineItemFormData;
import com.commercetools.sunrise.framework.cart.removelineitem.SunriseRemoveLineItemController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class RemoveLineItemController extends SunriseRemoveLineItemController {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public RemoveLineItemController(final TemplateRenderer templateRenderer,
                                    final FormFactory formFactory,
                                    final RemoveLineItemFormData formData,
                                    final CartFinder cartFinder,
                                    final RemoveLineItemControllerAction removeLineItemControllerAction,
                                    final CartDetailPageContentFactory cartDetailPageContentFactory,
                                    final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, formData, cartFinder, removeLineItemControllerAction, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final RemoveLineItemFormData formData) {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }
}
