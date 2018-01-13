package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

/**
 * Controller action to remove a discount code to a cart.
 */
@ImplementedBy(DefaultRemoveDiscountCodeFormAction.class)
@FunctionalInterface
public interface RemoveDiscountCodeFormAction extends FormAction<RemoveDiscountCodeFormData> {

}
