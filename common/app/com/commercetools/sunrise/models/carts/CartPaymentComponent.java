package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctprequests.CartQueryHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.PaymentInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This controller component expands the carts payment info with the payments.
 *
 * @see Cart#getPaymentInfo()
 * @see CartExpansionModel#paymentInfo()
 * @see PaymentInfoExpansionModel#payments()
 */
public final class CartPaymentComponent implements ControllerComponent, CartQueryHook, CartUpdateCommandHook {

    @Override
    public CompletionStage<CartQuery> onCartQuery(final CartQuery query) {
        return completedFuture(query.plusExpansionPaths(m -> m.paymentInfo().payments()));
    }

    @Override
    public CompletionStage<CartUpdateCommand> onCartUpdateCommand(final CartUpdateCommand command){
        return completedFuture(command.plusExpansionPaths(m -> m.paymentInfo().payments()));
    }
}
