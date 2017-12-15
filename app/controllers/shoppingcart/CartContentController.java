package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.carts.CartDiscountCodesExpansionControllerComponent;
import com.commercetools.sunrise.models.carts.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.content.SunriseCartContentController;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class,
        CartDiscountCodesExpansionControllerComponent.class,
        MiniWishlistControllerComponent.class
})
public final class CartContentController extends SunriseCartContentController {

    @Inject
    public CartContentController(final ContentRenderer contentRenderer,
                                 final CartFinder cartFinder,
                                 final CartPageContentFactory pageContentFactory) {
        super(contentRenderer, cartFinder, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
