package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.UserResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCartImpl.class)
public interface MyCart extends UserResourceInCache<Cart> {

    @Override
    CompletionStage<Optional<Cart>> get();
}
