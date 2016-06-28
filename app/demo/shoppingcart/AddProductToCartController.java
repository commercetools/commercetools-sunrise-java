package demo.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.shoppingcart.addtocart.SunriseAddProductToCartController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public final class AddProductToCartController extends SunriseAddProductToCartController {
    @Inject
    private ReverseRouter reverseRouter;

    @Override
    protected CompletableFuture<Result> successfulResult() {
        return completedFuture(redirect(reverseRouter.showCart(userContext().languageTag())));
    }
}
