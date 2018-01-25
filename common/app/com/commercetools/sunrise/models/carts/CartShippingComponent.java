package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.ShippingInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * This controller component expands the carts shipping info with the shipping methods.
 *
 * @see Cart#getShippingInfo()
 * @see CartExpansionModel#shippingInfo()
 * @see ShippingInfoExpansionModel#shippingMethod()
 */
public final class CartShippingComponent implements ControllerComponent, MyCartFetcherHook {

    @Override
    public CompletionStage<Optional<Cart>> on(final CartQuery request, final Function<CartQuery, CompletionStage<Optional<Cart>>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(c -> c.shippingInfo().shippingMethod()));
    }
}
