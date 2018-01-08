package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultChangeQuantityInCartControllerAction.class)
@FunctionalInterface
public interface ChangeQuantityInCartControllerAction extends ControllerAction {

    CompletionStage<Cart> apply(final ChangeQuantityInCartFormData formData);
}
