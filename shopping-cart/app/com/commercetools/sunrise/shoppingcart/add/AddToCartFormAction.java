package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultAddToCartFormAction.class)
@FunctionalInterface
public interface AddToCartFormAction extends FormAction {

    CompletionStage<Cart> apply(final AddToCartFormData formData);
}
