package com.commercetools.sunrise.framework.checkout.shipping;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Singleton
final class ShippingSettingsImpl implements ShippingSettings {

    private final SphereClient sphereClient;

    @Inject
    ShippingSettingsImpl(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<List<ShippingMethod>> getShippingMethods(final Cart cart) {
        return sphereClient.execute(ShippingMethodsByCartGet.of(cart));
    }
}
