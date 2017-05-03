package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctprequests.CartQueryHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.PaymentInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

/**
 * This controller component expands the carts payment info with the payments.
 *
 * @see Cart#getPaymentInfo()
 * @see CartExpansionModel#paymentInfo()
 * @see PaymentInfoExpansionModel#payments()
 */
public class CartPaymentInfoExpansionControllerComponent implements ControllerComponent, CartQueryHook {
    @Override
    public CartQuery onCartQuery(final CartQuery cartQuery) {
        return cartQuery.plusExpansionPaths(m -> m.paymentInfo().payments());
    }
}
