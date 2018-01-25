package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

@ImplementedBy(DefaultMyWishlistUpdater.class)
public interface MyWishlistUpdater extends ResourceUpdater<ShoppingList, ShoppingListUpdateCommand> {

    CompletionStage<ShoppingList> applyOrCreate(List<? extends UpdateAction<ShoppingList>> updateActions);

    default CompletionStage<ShoppingList> applyOrCreate(UpdateAction<ShoppingList> updateAction) {
        return applyOrCreate(singletonList(updateAction));
    }
}
