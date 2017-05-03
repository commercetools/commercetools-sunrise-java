package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctprequests.CartQueryHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.ShippingInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

/**
 * This controller component expands the carts shipping info with the shipping methods.
 *
 * @see Cart#getShippingInfo()
 * @see CartExpansionModel#shippingInfo()
 * @see ShippingInfoExpansionModel#shippingMethod()
 */
public class CartShippingInfoExpansionControllerComponent implements ControllerComponent, CartQueryHook {
    @Override
    public CartQuery onCartQuery(final CartQuery cartQuery) {
        return cartQuery.plusExpansionPaths(m -> m.shippingInfo().shippingMethod());
    }
}
