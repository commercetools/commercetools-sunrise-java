package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSetShippingFormAction.class)
@FunctionalInterface
public interface SetShippingFormAction extends FormAction<SetShippingFormData> {

}
