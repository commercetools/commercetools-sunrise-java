package controllers.hololist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.shoppinglist.ShoppingListFinder;
import com.commercetools.sunrise.shoppinglist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.shoppinglist.content.SunriseShoppingListContentController;
import com.commercetools.sunrise.shoppinglist.content.viewmodels.ShoppingListPageContentFactory;
import com.commercetools.sunrise.framework.reverserouters.hololist.HololistReverseRouter;
import controllers.wishlist.WishlistTypeIdentifier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        ShoppingListInSessionControllerComponent.class
})
public final class HololistContentController extends SunriseShoppingListContentController implements HololistTypeIdentifier {


    @Inject
    public HololistContentController(final ContentRenderer contentRenderer,
                                     final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                     final ShoppingListFinder shoppingListFinder) {
        super(contentRenderer, shoppingListPageContentFactory, shoppingListFinder);
    }


    @EnableHooks
    @SunriseRoute(HololistReverseRouter.HOLOLIST_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireShoppingList(this::showPage, getShoppingListType());
    }


    @Override
    public String getTemplateName() {
        return "my-account-holograms";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
