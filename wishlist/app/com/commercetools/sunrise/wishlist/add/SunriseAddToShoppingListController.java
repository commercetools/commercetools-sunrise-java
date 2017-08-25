package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.ShoppingListCreator;
import com.commercetools.sunrise.wishlist.ShoppingListFinder;
import com.commercetools.sunrise.wishlist.ShoppingListTypeIdentifier;
import com.commercetools.sunrise.wishlist.WithRequiredShoppingList;
import com.commercetools.sunrise.wishlist.content.viewmodels.ShoppingListPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddToShoppingListController extends SunriseContentFormController
        implements WithContentFormFlow<ShoppingList, ShoppingList, AddToShoppingListFormData>, WithRequiredShoppingList,ShoppingListTypeIdentifier {
    private final AddToShoppingListFormData formData;
    private final ShoppingListPageContentFactory shoppingListPageContentFactory;
    private final ShoppingListCreator shoppingListCreator;
    private final ShoppingListFinder shoppingListFinder;
    private final AddToShoppingListControllerAction controllerAction;

    @Inject
    protected SunriseAddToShoppingListController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                 final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                                 final AddToShoppingListFormData formData,
                                                 final ShoppingListCreator shoppingListCreator,
                                                 final ShoppingListFinder shoppingListFinder,
                                                 final AddToShoppingListControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.shoppingListPageContentFactory = shoppingListPageContentFactory;
        this.formData = formData;
        this.shoppingListCreator = shoppingListCreator;
        this.shoppingListFinder = shoppingListFinder;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddToShoppingListFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final ShoppingListFinder getShoppingListFinder() {
        return shoppingListFinder;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.ADD_TO_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireShoppingList(this::processForm, getShoppingListType());
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist, final AddToShoppingListFormData formData) {
        return controllerAction.apply(wishlist, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final AddToShoppingListFormData formData);

    @Override
    public PageContent createPageContent(final ShoppingList wishlist, final Form<? extends AddToShoppingListFormData> formData) {
        return shoppingListPageContentFactory.create(wishlist);
    }

    @Override
    public CompletionStage<Result> handleNotFoundShoppingList() {
        return shoppingListCreator.get(getShoppingListType())
                .thenComposeAsync(this::processForm, HttpExecution.defaultContext());
    }

    @Override
    public void preFillFormData(final ShoppingList wishlist, final AddToShoppingListFormData formData) {
      // Do not pre-fill anything
    }
}
