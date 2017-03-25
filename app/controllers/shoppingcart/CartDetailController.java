package controllers.shoppingcart;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.cart.cartdetail.SunriseCartDetailController;
import com.commercetools.sunrise.framework.cart.cartdetail.viewmodels.CartDetailPageContentFactory;
import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;

import javax.annotation.Nullable;
import javax.inject.Inject;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class CartDetailController extends SunriseCartDetailController {

    @Inject
    public CartDetailController(final ContentRenderer contentRenderer,
                                final CartFinder cartFinder,
                                final CartDetailPageContentFactory pageContentFactory) {
        super(contentRenderer, cartFinder, pageContentFactory);
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "cart";
    }
}
