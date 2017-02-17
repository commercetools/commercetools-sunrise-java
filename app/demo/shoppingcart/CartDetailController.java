package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.SunriseCartDetailController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;

import javax.inject.Inject;

@NoCache
public final class CartDetailController extends SunriseCartDetailController {

    @Inject
    public CartDetailController(final ComponentRegistry componentRegistry,
                                final TemplateRenderer templateRenderer,
                                final CartFinder cartFinder,
                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(componentRegistry, templateRenderer, cartFinder, cartDetailPageContentFactory);
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
    }
}
