package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

/**
 * Controller action to add a discount code to a cart.
 */
@ImplementedBy(DefaultAddDiscountCodeControllerAction.class)
@FunctionalInterface
public interface AddDiscountCodeControllerAction  extends ControllerAction, BiFunction<Cart, AddDiscountCodeFormData, CompletionStage<Cart>> {
    @Override
    CompletionStage<Cart> apply(Cart cart, AddDiscountCodeFormData addDiscountCodeFormData);
}
