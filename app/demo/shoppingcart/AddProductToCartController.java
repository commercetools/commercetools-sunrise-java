package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.AddProductToCartFunction;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.DefaultAddProductToCartFormData;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.SunriseAddProductToCartController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class AddProductToCartController extends SunriseAddProductToCartController<DefaultAddProductToCartFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddProductToCartController(final CartCreator cartCreator,
                                      final CartFinder cartFinder,
                                      final AddProductToCartFunction addProductToCartFunction,
                                      final CartDetailPageContentFactory cartDetailPageContentFactory,
                                      final CartReverseRouter cartReverseRouter) {
        super(cartCreator, cartFinder, addProductToCartFunction, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultAddProductToCartFormData> getFormDataClass() {
        return DefaultAddProductToCartFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultAddProductToCartFormData formData, final Cart oldCart, final Cart updatedCart) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
