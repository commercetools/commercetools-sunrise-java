package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Controller action to remove a discount code to a cart.
 */
@ImplementedBy(DefaultRemoveDiscountCodeFormAction.class)
@FunctionalInterface
public interface RemoveDiscountCodeFormAction extends FormAction {

    CompletionStage<Cart> apply(RemoveDiscountCodeFormData removeDiscountCodeFormData);
}
