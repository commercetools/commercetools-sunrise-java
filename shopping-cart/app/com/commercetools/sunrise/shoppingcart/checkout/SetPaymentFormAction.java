package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSetPaymentFormAction.class)
@FunctionalInterface
public interface SetPaymentFormAction extends FormAction<SetPaymentFormData> {

}
