package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.DefaultRemoveLineItemFormData;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.RemoveLineItemExecutor;
import com.commercetools.sunrise.shoppingcart.cart.removelineitem.SunriseRemoveLineItemController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class RemoveLineItemController extends SunriseRemoveLineItemController<DefaultRemoveLineItemFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public RemoveLineItemController(final ComponentRegistry componentRegistry,
                                    final TemplateRenderer templateRenderer,
                                    final FormFactory formFactory,
                                    final CartFinder cartFinder,
                                    final RemoveLineItemExecutor removeLineItemExecutor,
                                    final CartDetailPageContentFactory cartDetailPageContentFactory,
                                    final CartReverseRouter cartReverseRouter) {
        super(componentRegistry, templateRenderer, formFactory, cartFinder, removeLineItemExecutor, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public Class<DefaultRemoveLineItemFormData> getFormDataClass() {
        return DefaultRemoveLineItemFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultRemoveLineItemFormData formData) {
        return redirectTo(cartReverseRouter.showCart());
    }
}
