package com.commercetools.sunrise.shoppingcart.cart.addlineitem;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.concurrent.CompletionStage;

public class DefaultCartCreator extends AbstractCartCreateExecutor implements CartCreator {

    private final CurrencyUnit currency;

    @Inject
    protected DefaultCartCreator(final SphereClient sphereClient, final HookRunner hookRunner, final CurrencyUnit currency) {
        super(sphereClient, hookRunner);
        this.currency = currency;
    }

    @Override
    public CompletionStage<Cart> get() {
        return executeRequest(buildRequest());
    }

    protected CartCreateCommand buildRequest() {
        final CartDraft cartDraft = CartDraftBuilder.of(currency).build();
        return CartCreateCommand.of(cartDraft);
    }
}
