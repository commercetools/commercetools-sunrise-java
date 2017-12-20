package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestStartedHook;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.wishlists.WishlistInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class MiniWishlistControllerComponent implements ControllerComponent, PageDataReadyHook, HttpRequestStartedHook {
    private final WishlistInSession wishlistInSession;
    private final WishlistFinder wishlistFinder;
    @Nullable
    private ShoppingList shoppingList;

    @Inject
    protected MiniWishlistControllerComponent(final WishlistInSession wishlistInSession, final WishlistFinderBySession wishlistFinder) {
        this.wishlistInSession = wishlistInSession;
        this.wishlistFinder = wishlistFinder;
    }

    @Override
    public CompletionStage<?> onHttpRequestStarted(Http.Context httpContext) {
        Optional<ShoppingList> optional = wishlistInSession.findWishlist();
        final CompletionStage<Optional<ShoppingList>> stage;
        if (optional.isPresent()) {
            stage = CompletableFuture.completedFuture(optional);
        } else {
            stage = wishlistFinder.get();
        }
        return stage.thenAccept(o -> {
            o.ifPresent(wishlistInSession::store);//TODO why does not the wishlistFinder store it???
            this.shoppingList = o.orElse(null);
        });
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getContent().put("wishlist", shoppingList); // we use a dynamic property here, because we don't want to extend PageContent
    }
}
