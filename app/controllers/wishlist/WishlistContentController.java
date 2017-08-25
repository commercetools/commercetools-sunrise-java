package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.ShoppingListFinder;
import com.commercetools.sunrise.wishlist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.content.SunriseShoppingListContentController;
import com.commercetools.sunrise.wishlist.content.viewmodels.ShoppingListPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        ShoppingListInSessionControllerComponent.class
})
public final class WishlistContentController extends SunriseShoppingListContentController implements WishlistTypeIdentifier {


    @Inject
    public WishlistContentController(final ContentRenderer contentRenderer,
                                     final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                     final ShoppingListFinder shoppingListFinder) {
        super(contentRenderer, shoppingListPageContentFactory, shoppingListFinder);
    }

    @Override
    public String getTemplateName() {
        return "my-account-wishlist";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
