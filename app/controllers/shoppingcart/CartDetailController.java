package controllers.shoppingcart;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.SunriseCartDetailController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentsSupplier;
import controllers.PageHeaderControllerComponentsSupplier;

import javax.inject.Inject;

@NoCache
@RegisteredComponents({
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class CartDetailController extends SunriseCartDetailController {

    @Inject
    public CartDetailController(final TemplateRenderer templateRenderer,
                                final CartFinder cartFinder,
                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, cartFinder, cartDetailPageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }
}
