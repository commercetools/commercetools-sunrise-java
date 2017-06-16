package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WithRequiredWishlist;
import com.commercetools.sunrise.wishlist.viewmodels.RemoveWishlistLineItemFormData;
import com.commercetools.sunrise.wishlist.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromWishlistController extends SunriseContentFormController
        implements WithContentFormFlow<ShoppingList, ShoppingList, RemoveWishlistLineItemFormData>, WithRequiredWishlist {
    private final RemoveWishlistLineItemFormData formData;
    private final WishlistPageContentFactory wishlistPageContentFactory;
    private final WishlistFinder wishlistFinder;
    private final RemoveFromWishlistControllerAction controllerAction;

    @Inject
    protected SunriseRemoveFromWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                  final WishlistPageContentFactory wishlistPageContentFactory,
                                                  final RemoveWishlistLineItemFormData formData,
                                                  final WishlistFinder wishlistFinder,
                                                  final RemoveFromWishlistControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.wishlistPageContentFactory = wishlistPageContentFactory;
        this.formData = formData;
        this.wishlistFinder = wishlistFinder;
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.REMOVE_FROM_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireWishlist(this::processForm);
    }

    @Override
    public Class<? extends RemoveWishlistLineItemFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistLineItemFormData) {
        return controllerAction.apply(wishlist, removeWishlistLineItemFormData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistLineItemFormData);

    @Override
    public PageContent createPageContent(final ShoppingList wishlist, final Form<? extends RemoveWishlistLineItemFormData> removeWishlistLineItemFormData) {
        return wishlistPageContentFactory.create(wishlist);
    }

    @Override
    public void preFillFormData(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistLineItemFormData) {

    }

    @Override
    public WishlistFinder getWishlistFinder() {
        return wishlistFinder;
    }
}
