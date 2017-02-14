package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.AddProductToCartExecutor;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.DefaultAddProductToCartFormData;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.SunriseAddProductToCartController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class AddProductToCartController extends SunriseAddProductToCartController<DefaultAddProductToCartFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddProductToCartController(final RequestHookContext hookContext,
                                      final TemplateRenderer templateRenderer,
                                      final FormFactory formFactory,
                                      final CartFinder cartFinder,
                                      final CartCreator cartCreator,
                                      final AddProductToCartExecutor addProductToCartExecutor,
                                      final CartDetailPageContentFactory cartDetailPageContentFactory,
                                      final CartReverseRouter cartReverseRouter) {
        super(hookContext, templateRenderer, formFactory, cartFinder, cartCreator, addProductToCartExecutor, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultAddProductToCartFormData> getFormDataClass() {
        return DefaultAddProductToCartFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultAddProductToCartFormData formData) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
