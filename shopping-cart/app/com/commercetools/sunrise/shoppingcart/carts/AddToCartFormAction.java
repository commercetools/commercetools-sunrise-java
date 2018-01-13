package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddToCartFormAction.class)
@FunctionalInterface
public interface AddToCartFormAction extends FormAction<AddToCartFormData> {

}
