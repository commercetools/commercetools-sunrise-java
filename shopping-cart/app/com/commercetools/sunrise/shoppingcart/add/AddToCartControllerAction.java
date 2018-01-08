package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultAddToCartControllerAction.class)
@FunctionalInterface
public interface AddToCartControllerAction extends ControllerAction {

    CompletionStage<Cart> apply(final AddToCartFormData formData);
}
