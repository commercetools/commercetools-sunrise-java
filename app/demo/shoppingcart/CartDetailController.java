package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.SunriseCartDetailController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;

import javax.inject.Inject;

@NoCache
@RegisteredComponents({
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
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
