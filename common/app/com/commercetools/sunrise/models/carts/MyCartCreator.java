package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCartCreator.class)
public interface MyCartCreator extends ResourceCreator<Cart, CartDraft> {

    CompletionStage<CartDraft> defaultDraft();

    default CompletionStage<Cart> get() {
        return defaultDraft().thenComposeAsync(this::get, HttpExecution.defaultContext());
    }
}
