package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultChangeQuantityInCartFormAction.class)
@FunctionalInterface
public interface ChangeQuantityInCartFormAction extends FormAction {

    CompletionStage<Cart> apply(final ChangeQuantityInCartFormData formData);
}
