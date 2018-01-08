package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.controllers.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCartCreator.class)
@FunctionalInterface
public interface CartCreator extends ResourceCreator<Cart> {

    CompletionStage<Cart> get(@Nullable CartDraft template);

    default CompletionStage<Cart> get() {
        return get(null);
    }
}
