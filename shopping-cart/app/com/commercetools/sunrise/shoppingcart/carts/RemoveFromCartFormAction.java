package com.commercetools.sunrise.shoppingcart.carts;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveFromCartFormAction.class)
@FunctionalInterface
public interface RemoveFromCartFormAction extends FormAction<RemoveFromCartFormData> {

}
