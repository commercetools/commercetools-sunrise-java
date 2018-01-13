package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCheckoutAddressFormAction.class)
@FunctionalInterface
public interface CheckoutAddressFormAction extends FormAction<CheckoutAddressFormData> {

}
