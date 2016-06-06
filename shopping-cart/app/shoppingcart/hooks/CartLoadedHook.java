package shoppingcart.hooks;

import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface CartLoadedHook {
    CompletionStage<Object> cartLoaded(final Cart cart);
}
