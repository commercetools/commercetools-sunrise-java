package controllers.shoppingcart;

import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.DefaultRemoveLineItemFormData;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.RemoveLineItemControllerAction;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.SunriseRemoveLineItemController;
import controllers.PageHeaderControllerComponentsSupplier;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class RemoveLineItemController extends SunriseRemoveLineItemController<DefaultRemoveLineItemFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public RemoveLineItemController(final TemplateRenderer templateRenderer,
                                    final FormFactory formFactory,
                                    final CartFinder cartFinder,
                                    final RemoveLineItemControllerAction removeLineItemControllerAction,
                                    final CartDetailPageContentFactory cartDetailPageContentFactory,
                                    final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, removeLineItemControllerAction, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public Class<DefaultRemoveLineItemFormData> getFormDataClass() {
        return DefaultRemoveLineItemFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultRemoveLineItemFormData formData) {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }
}
