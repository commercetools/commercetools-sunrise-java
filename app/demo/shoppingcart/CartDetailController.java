package demo.shoppingcart;

import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.SunriseCartDetailController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;

import javax.inject.Inject;

public final class CartDetailController extends SunriseCartDetailController {

    @Inject
    public CartDetailController(final TemplateRenderer templateRenderer,
                                final RequestHookContext hookContext,
                                final CartFinder cartFinder,
                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, hookContext, cartFinder, cartDetailPageContentFactory);
    }
}
