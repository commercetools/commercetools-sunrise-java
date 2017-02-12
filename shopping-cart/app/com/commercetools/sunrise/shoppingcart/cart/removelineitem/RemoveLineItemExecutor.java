package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveLineItemExecutor.class)
@FunctionalInterface
public interface RemoveLineItemExecutor extends BiFunction<Cart, RemoveLineItemFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final RemoveLineItemFormData formData);
}
