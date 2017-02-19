package controllers.shoppingcart;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.ChangeLineItemQuantityControllerAction;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.DefaultChangeLineItemQuantityFormData;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.SunriseChangeLineItemQuantityController;
import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
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
public final class ChangeLineItemQuantityController extends SunriseChangeLineItemQuantityController<DefaultChangeLineItemQuantityFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public ChangeLineItemQuantityController(final TemplateRenderer templateRenderer,
                                            final FormFactory formFactory,
                                            final CartFinder cartFinder,
                                            final CartDetailPageContentFactory cartDetailPageContentFactory,
                                            final ChangeLineItemQuantityControllerAction changeLineItemQuantityControllerAction,
                                            final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, cartDetailPageContentFactory, changeLineItemQuantityControllerAction);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public Class<DefaultChangeLineItemQuantityFormData> getFormDataClass() {
        return DefaultChangeLineItemQuantityFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultChangeLineItemQuantityFormData formData) {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }
}
