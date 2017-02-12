package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangeLineItemQuantityExecutor.class)
@FunctionalInterface
public interface ChangeLineItemQuantityExecutor extends BiFunction<Cart, ChangeLineItemQuantityFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final ChangeLineItemQuantityFormData formData);
}
