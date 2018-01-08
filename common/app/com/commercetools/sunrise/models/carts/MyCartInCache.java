package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCartInCacheImpl.class)
public interface MyCartInCache extends ResourceInCache<Cart> {

    @Override
    CompletionStage<Optional<Cart>> get();

    @Override
    void store(@Nullable final Cart cart);

    @Override
    void remove();
}
