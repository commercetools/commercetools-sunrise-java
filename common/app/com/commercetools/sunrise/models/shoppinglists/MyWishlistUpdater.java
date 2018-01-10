package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyWishlistUpdaterImpl.class)
@FunctionalInterface
public interface MyWishlistUpdater extends ResourceUpdater<ShoppingList> {

    @Override
    CompletionStage<Optional<ShoppingList>> apply(List<? extends UpdateAction<ShoppingList>> updateActions);
}
