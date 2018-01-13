package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

/**
 * Controller action to add a discount code to a cart.
 */
@ImplementedBy(DefaultAddDiscountCodeFormAction.class)
@FunctionalInterface
public interface AddDiscountCodeFormAction extends FormAction<AddDiscountCodeFormData> {

}
