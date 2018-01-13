package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangeQuantityInCartFormAction.class)
@FunctionalInterface
public interface ChangeQuantityInCartFormAction extends FormAction<ChangeQuantityInCartFormData> {

}
