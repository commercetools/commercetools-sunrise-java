package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.ShoppingListFinder;
import com.commercetools.sunrise.wishlist.ShoppingListTypeIdentifier;
import com.commercetools.sunrise.wishlist.WithRequiredShoppingList;
import com.commercetools.sunrise.wishlist.content.viewmodels.ShoppingListPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromShoppingListController extends SunriseContentFormController
        implements WithContentFormFlow<ShoppingList, ShoppingList, RemoveFromShoppingListFormData>, WithRequiredShoppingList, ShoppingListTypeIdentifier {
    private final RemoveFromShoppingListFormData formData;
    private final ShoppingListPageContentFactory shoppingListPageContentFactory;
    private final ShoppingListFinder shoppingListFinder;
    private final RemoveFromShoppingListControllerAction controllerAction;

    @Inject
    protected SunriseRemoveFromShoppingListController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                      final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                                      final RemoveFromShoppingListFormData formData,
                                                      final ShoppingListFinder shoppingListFinder,
                                                      final RemoveFromShoppingListControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.shoppingListPageContentFactory = shoppingListPageContentFactory;
        this.formData = formData;
        this.shoppingListFinder = shoppingListFinder;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends RemoveFromShoppingListFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final ShoppingListFinder getShoppingListFinder() {
        return shoppingListFinder;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.REMOVE_FROM_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireShoppingList(this::processForm, getShoppingListType());
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist, final RemoveFromShoppingListFormData formData) {
        return controllerAction.apply(wishlist, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final RemoveFromShoppingListFormData formData);

    @Override
    public PageContent createPageContent(final ShoppingList wishlist, final Form<? extends RemoveFromShoppingListFormData> formData) {
        return shoppingListPageContentFactory.create(wishlist);
    }

    @Override
    public void preFillFormData(final ShoppingList wishlist, final RemoveFromShoppingListFormData formData) {
        // Do not pre-fill anything
    }
}
