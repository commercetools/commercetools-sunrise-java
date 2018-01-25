package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCartCreator.class)
public interface MyCartCreator extends ResourceCreator {

    CompletionStage<Cart> get();
}
