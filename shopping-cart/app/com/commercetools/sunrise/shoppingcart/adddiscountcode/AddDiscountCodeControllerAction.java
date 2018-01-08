package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Controller action to add a discount code to a cart.
 */
@ImplementedBy(DefaultAddDiscountCodeControllerAction.class)
@FunctionalInterface
public interface AddDiscountCodeControllerAction  extends ControllerAction {

    CompletionStage<Cart> apply(AddDiscountCodeFormData addDiscountCodeFormData);
}
