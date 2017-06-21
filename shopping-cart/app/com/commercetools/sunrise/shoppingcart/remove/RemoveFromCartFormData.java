package com.commercetools.sunrise.shoppingcart.remove;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveFromCartFormData.class)
public interface RemoveFromCartFormData {

    String lineItemId();
}
