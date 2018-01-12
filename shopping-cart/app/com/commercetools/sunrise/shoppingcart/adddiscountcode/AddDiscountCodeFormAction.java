package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Controller action to add a discount code to a cart.
 */
@ImplementedBy(DefaultAddDiscountCodeFormAction.class)
@FunctionalInterface
public interface AddDiscountCodeFormAction extends FormAction {

    CompletionStage<Cart> apply(AddDiscountCodeFormData addDiscountCodeFormData);
}
