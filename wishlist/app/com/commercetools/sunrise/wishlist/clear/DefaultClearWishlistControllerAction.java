package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.models.shoppinglists.MyWishlistInCache;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class DefaultClearWishlistControllerAction implements ClearWishlistControllerAction {

    private final MyWishlistUpdater myWishlistUpdater;
    private final MyWishlistInCache myWishlistInCache;

    @Inject
    protected DefaultClearWishlistControllerAction(final MyWishlistUpdater myWishlistUpdater, final MyWishlistInCache myWishlistInCache) {
        this.myWishlistUpdater = myWishlistUpdater;
        this.myWishlistInCache = myWishlistInCache;
    }

    @Override
    public CompletionStage<ShoppingList> apply() {
        return myWishlistInCache.require()
                .thenComposeAsync(wishlist -> myWishlistUpdater.force(updateActions(wishlist)), HttpExecution.defaultContext());
    }

    protected List<? extends UpdateAction<ShoppingList>> updateActions(final ShoppingList wishlist) {
        return Optional.ofNullable(wishlist.getLineItems())
                .map(lineItems -> lineItems.stream()
                        .map(RemoveLineItem::of)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }
}
