package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultRemoveFromCartFormAction.class)
@FunctionalInterface
public interface RemoveFromCartFormAction extends FormAction {

    CompletionStage<Cart> apply(final RemoveFromCartFormData formData);
}
