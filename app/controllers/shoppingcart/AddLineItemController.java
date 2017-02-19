package controllers.shoppingcart;

import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.shoppingcart.cart.addlineitem.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.addlineitem.AddLineItemControllerAction;
import com.commercetools.sunrise.shoppingcart.cart.addlineitem.DefaultAddLineItemFormData;
import com.commercetools.sunrise.shoppingcart.cart.addlineitem.SunriseAddLineItemController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
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
public final class AddLineItemController extends SunriseAddLineItemController<DefaultAddLineItemFormData> {

    private final CartReverseRouter cartReverseRouter;

    @Inject
    public AddLineItemController(final TemplateRenderer templateRenderer,
                                 final FormFactory formFactory,
                                 final CartFinder cartFinder,
                                 final CartCreator cartCreator,
                                 final AddLineItemControllerAction addLineItemControllerAction,
                                 final CartDetailPageContentFactory cartDetailPageContentFactory,
                                 final CartReverseRouter cartReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, cartCreator, addLineItemControllerAction, cartDetailPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public Class<DefaultAddLineItemFormData> getFormDataClass() {
        return DefaultAddLineItemFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultAddLineItemFormData formData) {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }
}
