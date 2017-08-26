package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.shoppinglist.ShoppingListCreator;
import com.commercetools.sunrise.shoppinglist.ShoppingListFinder;
import com.commercetools.sunrise.shoppinglist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.shoppinglist.add.AddToShoppingListControllerAction;
import com.commercetools.sunrise.shoppinglist.add.AddToShoppingListFormData;
import com.commercetools.sunrise.shoppinglist.add.SunriseAddToShoppingListController;
import com.commercetools.sunrise.shoppinglist.content.viewmodels.ShoppingListPageContentFactory;
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

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.ADD_TO_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireShoppingList(this::processForm, getShoppingListType());
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
