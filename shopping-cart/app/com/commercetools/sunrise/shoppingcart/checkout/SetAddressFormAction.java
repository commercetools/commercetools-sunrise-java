package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSetAddressFormAction.class)
@FunctionalInterface
public interface SetAddressFormAction extends FormAction<SetAddressFormData> {

}
