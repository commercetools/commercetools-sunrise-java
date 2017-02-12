package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.ChangeLineItemQuantityExecutor;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.DefaultChangeLineItemQuantityFormData;
import com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity.SunriseChangeLineItemQuantityController;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class ChangeLineItemQuantityController extends SunriseChangeLineItemQuantityController<DefaultChangeLineItemQuantityFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public ChangeLineItemQuantityController(final CartFinder cartFinder,
                                            final CartDetailPageContentFactory cartDetailPageContentFactory,
                                            final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor,
                                            final CartReverseRouter cartReverseRouter) {
        super(cartFinder, cartDetailPageContentFactory, changeLineItemQuantityExecutor);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultChangeLineItemQuantityFormData> getFormDataClass() {
        return DefaultChangeLineItemQuantityFormData.class;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultChangeLineItemQuantityFormData formData, final Cart oldCart, final Cart updatedCart) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
