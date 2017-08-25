package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.ShoppingListCreator;
import com.commercetools.sunrise.wishlist.ShoppingListFinder;
import com.commercetools.sunrise.wishlist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.add.AddToShoppingListControllerAction;
import com.commercetools.sunrise.wishlist.add.AddToShoppingListFormData;
import com.commercetools.sunrise.wishlist.add.SunriseAddToShoppingListController;
import com.commercetools.sunrise.wishlist.content.viewmodels.ShoppingListPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
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
public final class AddToWishlistController extends SunriseAddToShoppingListController implements WishlistTypeIdentifier {

    private final WishlistReverseRouter reverseRouter;

    @Inject
    public AddToWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                   final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                   final AddToShoppingListFormData formData, final ShoppingListCreator shoppingListCreator, final ShoppingListFinder shoppingListFinder,
                                   final AddToShoppingListControllerAction controllerAction, final WishlistReverseRouter reverseRouter) {
        super(contentRenderer, formFactory, shoppingListPageContentFactory, formData, shoppingListCreator, shoppingListFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-wishlist";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final AddToShoppingListFormData addToShoppingListFormData) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
