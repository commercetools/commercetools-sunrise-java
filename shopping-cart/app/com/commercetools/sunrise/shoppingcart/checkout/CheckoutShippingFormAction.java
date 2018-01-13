package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCheckoutShippingFormAction.class)
@FunctionalInterface
public interface CheckoutShippingFormAction extends FormAction<CheckoutShippingFormData> {

}
