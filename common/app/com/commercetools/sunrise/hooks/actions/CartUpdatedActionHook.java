package com.commercetools.sunrise.hooks.actions;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public interface CartUpdatedActionHook extends ActionHook {

    CompletionStage<Cart> onCartUpdatedAction(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer);

    static CompletionStage<Cart> runHook(final HookRunner hookRunner, final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return hookRunner.runActionHook(CartUpdatedActionHook.class, (hook, updatedCart) -> hook.onCartUpdatedAction(updatedCart, expansionPathContainer), cart);
    }

}
