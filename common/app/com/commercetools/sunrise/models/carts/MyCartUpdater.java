package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.controllers.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.commands.UpdateAction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCartUpdaterImpl.class)
@FunctionalInterface
public interface MyCartUpdater extends ResourceUpdater<Cart> {

    @Override
    CompletionStage<Optional<Cart>> apply(List<? extends UpdateAction<Cart>> updateActions);
}
