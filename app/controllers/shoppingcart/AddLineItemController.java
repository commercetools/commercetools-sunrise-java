package controllers.shoppingcart;

import com.commercetools.sunrise.common.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.AddProductToCartExecutor;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.DefaultAddProductToCartFormData;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.SunriseAddLineItemController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
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
public final class AddLineItemController extends SunriseAddLineItemController<DefaultAddProductToCartFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddLineItemController(final TemplateRenderer templateRenderer,
                                 final FormFactory formFactory,
                                 final CartFinder cartFinder,
                                 final CartCreator cartCreator,
                                 final AddProductToCartExecutor addProductToCartExecutor,
                                 final CartDetailPageContentFactory cartDetailPageContentFactory,
                                 final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, cartCreator, addProductToCartExecutor, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public Class<DefaultAddProductToCartFormData> getFormDataClass() {
        return DefaultAddProductToCartFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultAddProductToCartFormData formData) {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }
}
