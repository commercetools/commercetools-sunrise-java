package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.DefaultRemoveLineItemFormData;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.RemoveLineItemExecutor;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.SunriseRemoveLineItemController;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class RemoveLineItemController extends SunriseRemoveLineItemController<DefaultRemoveLineItemFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public RemoveLineItemController(final CartFinder cartFinder,
                                    final RemoveLineItemExecutor removeLineItemExecutor,
                                    final CartDetailPageContentFactory cartDetailPageContentFactory,
                                    final CartReverseRouter cartReverseRouter) {
        super(cartFinder, removeLineItemExecutor, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultRemoveLineItemFormData> getFormDataClass() {
        return DefaultRemoveLineItemFormData.class;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultRemoveLineItemFormData formData, final Cart oldCart, final Cart updatedCart) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
