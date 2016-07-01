package demo.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.SunriseAddProductToCartController;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public final class AddProductToCartController extends SunriseAddProductToCartController {

    @Override
    protected CompletableFuture<Result> successfulResult() {
        return completedFuture(redirect(injector().getInstance(CartReverseRouter.class).showCart(userContext().languageTag())));
    }
}
