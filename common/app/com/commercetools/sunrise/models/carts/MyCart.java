package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCartImpl.class)
public interface MyCart extends ResourceInCache<Cart> {

    @Override
    CompletionStage<Optional<Cart>> get();
}
