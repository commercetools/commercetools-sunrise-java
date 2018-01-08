package com.commercetools.sunrise.shoppingcart.remove;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;

@ImplementedBy(DefaultRemoveFromCartFormData.class)
public interface RemoveFromCartFormData {

    default RemoveLineItem removeLineItem() {
        return RemoveLineItem.of(lineItemId());
    }

    String lineItemId();
}
