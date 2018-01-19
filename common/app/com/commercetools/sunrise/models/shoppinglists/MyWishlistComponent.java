package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListQueryHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListUpdateCommandHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.google.inject.Inject;
import io.sphere.sdk.expansion.ExpansionPath;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.expansion.ShoppingListExpansionModel;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class MyWishlistComponent implements ControllerComponent, PageDataHook, ShoppingListQueryHook, ShoppingListUpdateCommandHook {

    private final MyWishlist myWishlist;

    @Inject
    MyWishlistComponent(final MyWishlist myWishlist) {
        this.myWishlist = myWishlist;
    }

    @Override
    public CompletionStage<ShoppingListQuery> onShoppingListQuery(final ShoppingListQuery query) {
        return completedFuture(query.plusExpansionPaths(productInformationExpansion()));
    }

    @Override
    public CompletionStage<ShoppingListUpdateCommand> onShoppingListUpdateCommand(final ShoppingListUpdateCommand command) {
        return completedFuture(command.plusExpansionPaths(productInformationExpansion()));
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
