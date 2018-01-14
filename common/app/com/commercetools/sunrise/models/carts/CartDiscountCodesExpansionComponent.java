package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctprequests.CartQueryHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.expansion.CartExpansionModel;
import io.sphere.sdk.carts.expansion.DiscountCodeInfoExpansionModel;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This controller component expands the carts discount code infos with the discount codes.
 *
 * @see Cart#getDiscountCodes() ()
 * @see CartExpansionModel#discountCodes()
 * @see DiscountCodeInfoExpansionModel#discountCode
 */
public final class CartDiscountCodesExpansionComponent implements ControllerComponent, CartQueryHook, CartUpdateCommandHook {

    @Override
    public CompletionStage<CartQuery> onCartQuery(final CartQuery query) {
        return completedFuture(query.plusExpansionPaths(m -> m.discountCodes().discountCode()));
    }

    @Override
    public CompletionStage<CartUpdateCommand> onCartUpdateCommand(final CartUpdateCommand command){
        return completedFuture(command.plusExpansionPaths(m -> m.discountCodes().discountCode()));
    }
}
