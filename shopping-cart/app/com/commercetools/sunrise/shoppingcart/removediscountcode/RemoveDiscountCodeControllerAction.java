package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

/**
 * Controller action to remove a discount code to a cart.
 */
@ImplementedBy(DefaultRemoveDiscountCodeControllerAction.class)
@FunctionalInterface
public interface RemoveDiscountCodeControllerAction extends ControllerAction, BiFunction<Cart, RemoveDiscountCodeFormData, CompletionStage<Cart>> {
    @Override
    CompletionStage<Cart> apply(Cart cart, RemoveDiscountCodeFormData removeDiscountCodeFormData);
}
