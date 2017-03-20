package com.commercetools.sunrise.framework.hooks.ctpactions;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;

public interface CartLoadedActionHook extends CtpActionHook {

    CompletionStage<Cart> onCartLoadedAction(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer);

    static CompletionStage<Cart> runHook(final HookRunner hookRunner, final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return hookRunner.runActionHook(CartLoadedActionHook.class, (hook, updatedCart) -> hook.onCartLoadedAction(updatedCart, expansionPathContainer), cart);
    }

}
