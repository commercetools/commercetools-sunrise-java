package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.AddProductToCartExecutor;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.DefaultAddProductToCartFormData;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.SunriseAddProductToCartController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
})
public final class AddProductToCartController extends SunriseAddProductToCartController<DefaultAddProductToCartFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddProductToCartController(final TemplateRenderer templateRenderer,
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
        return redirectTo(cartReverseRouter.showCart());
    }
}
