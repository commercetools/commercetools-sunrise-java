package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.commands.UpdateAction;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

@ImplementedBy(DefaultMyCartUpdater.class)
public interface MyCartUpdater extends ResourceUpdater<Cart, CartUpdateCommand> {

    CompletionStage<Cart> applyOrCreate(List<? extends UpdateAction<Cart>> updateActions);

    default CompletionStage<Cart> applyOrCreate(UpdateAction<Cart> updateAction) {
        return applyOrCreate(singletonList(updateAction));
    }
}
