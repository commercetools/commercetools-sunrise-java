package com.commercetools.sunrise.shoppingcart.add;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddToCartFormData.class)
public interface AddToCartFormData {

    String productId();

    Integer variantId();

    Long quantity();
}
