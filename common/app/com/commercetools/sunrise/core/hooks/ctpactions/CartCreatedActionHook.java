package com.commercetools.sunrise.core.hooks.ctpactions;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public interface CartCreatedActionHook extends CtpActionHook {

    CompletionStage<Cart> onCartCreatedAction(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer);

    static CompletionStage<Cart> runHook(final HookRunner hookRunner, final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return hookRunner.run(CartCreatedActionHook.class, cart, (hook, createdCart) -> hook.onCartCreatedAction(createdCart, expansionPathContainer));
    }

}
