package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.ChangeLineItemQuantityExecutor;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.DefaultChangeLineItemQuantityFormData;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.SunriseChangeLineItemQuantityController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeLineItemQuantityController extends SunriseChangeLineItemQuantityController<DefaultChangeLineItemQuantityFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public ChangeLineItemQuantityController(final RequestHookContext hookContext,
                                            final TemplateRenderer templateRenderer,
                                            final FormFactory formFactory,
                                            final CartFinder cartFinder,
                                            final CartDetailPageContentFactory cartDetailPageContentFactory,
                                            final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor,
                                            final CartReverseRouter cartReverseRouter) {
        super(hookContext, templateRenderer, formFactory, cartFinder, cartDetailPageContentFactory, changeLineItemQuantityExecutor);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultChangeLineItemQuantityFormData> getFormDataClass() {
        return DefaultChangeLineItemQuantityFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultChangeLineItemQuantityFormData formData) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
