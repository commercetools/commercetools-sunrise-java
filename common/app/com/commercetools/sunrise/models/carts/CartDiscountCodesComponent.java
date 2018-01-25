package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class CartDiscountCodesComponent implements ControllerComponent, MyCartFetcherHook, MyCartUpdaterHook {

    @Override
    public CompletionStage<Optional<Cart>> on(final CartQuery request, final Function<CartQuery, CompletionStage<Optional<Cart>>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(c -> c.discountCodes().discountCode()));
    }

    @Override
    public CompletionStage<Cart> on(final CartUpdateCommand request, final Function<CartUpdateCommand, CompletionStage<Cart>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(c -> c.discountCodes().discountCode()));
    }
}
