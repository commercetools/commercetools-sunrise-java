package com.commercetools.sunrise.framework.checkout.shipping;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(ShippingSettingsImpl.class)
public interface ShippingSettings {

    CompletionStage<List<ShippingMethod>> getShippingMethods(final Cart cart);
}
