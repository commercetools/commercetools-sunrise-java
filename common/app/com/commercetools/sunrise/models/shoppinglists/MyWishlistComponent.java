package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.google.inject.Inject;
import io.sphere.sdk.expansion.ExpansionPath;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.expansion.ShoppingListExpansionModel;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class MyWishlistComponent implements ControllerComponent, PageDataHook, MyWishlistFetcherHook, MyWishlistUpdaterHook {

    private final MyWishlist myWishlist;

    @Inject
    MyWishlistComponent(final MyWishlist myWishlist) {
        this.myWishlist = myWishlist;
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> on(final ShoppingListQuery request, final Function<ShoppingListQuery, CompletionStage<Optional<ShoppingList>>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(productInformationExpansion()));
    }

    @Override
    public CompletionStage<ShoppingList> on(final ShoppingListUpdateCommand request, final Function<ShoppingListUpdateCommand, CompletionStage<ShoppingList>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(productInformationExpansion()));
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myWishlist.get()
                .thenApply(wishlistOpt -> wishlistOpt
                        .map(wishlist -> pageData.put("wishlist", wishlist))
                        .orElse(pageData));
    }

    private List<ExpansionPath<ShoppingList>> productInformationExpansion() {
        final List<ExpansionPath<ShoppingList>> expansionPaths = new ArrayList<>();
        expansionPaths.addAll(ShoppingListExpansionModel.of().lineItems().productSlug().expansionPaths());
        expansionPaths.addAll(ShoppingListExpansionModel.of().lineItems().variant().expansionPaths());
        return expansionPaths;
    }
}
