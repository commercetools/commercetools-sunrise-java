package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public interface PrimaryCartUpdateAsyncFilter extends Hook {
    CompletionStage<Cart> asyncFilterPrimaryCart(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer);
}
