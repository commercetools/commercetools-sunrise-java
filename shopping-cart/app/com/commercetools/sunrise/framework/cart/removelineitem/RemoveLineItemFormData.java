package com.commercetools.sunrise.framework.cart.removelineitem;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveLineItemFormData.class)
public interface RemoveLineItemFormData {

    String lineItemId();
}
