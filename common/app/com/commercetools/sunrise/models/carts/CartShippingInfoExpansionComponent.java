package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctprequests.CartQueryHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.ShippingInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This controller component expands the carts shipping info with the shipping methods.
 *
 * @see Cart#getShippingInfo()
 * @see CartExpansionModel#shippingInfo()
 * @see ShippingInfoExpansionModel#shippingMethod()
 */
public final class CartShippingInfoExpansionComponent implements ControllerComponent, CartQueryHook {

    @Override
    public CompletionStage<CartQuery> onCartQuery(final CartQuery query) {
        return completedFuture(query.plusExpansionPaths(m -> m.shippingInfo().shippingMethod()));
    }
}
