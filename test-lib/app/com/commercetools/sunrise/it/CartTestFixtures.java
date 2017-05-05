package com.commercetools.sunrise.it;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftDsl;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.models.DefaultCurrencyUnits;

import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.DEFAULT_DELETE_TTL;
import static com.commercetools.sunrise.it.TestFixtures.deleteWithRetry;

public final class CartTestFixtures {

    private CartTestFixtures() {
    }

    public static void withCart(final BlockingSphereClient client, final CartDraft cartDraft, final Function<Cart, Cart> test) {
        final Cart cart = client.executeBlocking(CartCreateCommand.of(cartDraft));
        final Cart cartAfterTest = test.apply(cart);
        deleteCartWithRetry(client, cartAfterTest);
    }

    public static CartDraftDsl cartDraft() {
        return CartDraft.of(DefaultCurrencyUnits.EUR);
    }

    public static void deleteCartWithRetry(final BlockingSphereClient client, final Cart cartAfterTest) {
        deleteWithRetry(client, cartAfterTest, CartDeleteCommand::of, DEFAULT_DELETE_TTL);
    }
}
