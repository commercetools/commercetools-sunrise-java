package com.commercetools.sunrise.shoppingcart.add;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;

@ImplementedBy(DefaultAddToCartFormData.class)
public interface AddToCartFormData {

    default AddLineItem addLineItem() {
        return AddLineItem.of(productId(), variantId(), quantity());
    }

    String productId();

    Integer variantId();

    Long quantity();
}
