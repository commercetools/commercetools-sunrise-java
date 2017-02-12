package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultAddProductToCartFunction.class)
@FunctionalInterface
public interface AddProductToCartFunction extends BiFunction<Cart, AddProductToCartFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final AddProductToCartFormData formData);
}
