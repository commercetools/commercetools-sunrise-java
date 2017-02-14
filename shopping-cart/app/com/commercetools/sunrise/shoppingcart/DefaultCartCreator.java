package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.hooks.events.CartCreatedHook;
import com.commercetools.sunrise.hooks.requests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.concurrent.CompletionStage;

public class DefaultCartCreator extends AbstractSphereRequestExecutor implements CartCreator {

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

    protected final CompletionStage<Cart> executeRequest(final CartCreateCommand baseCommand) {
        final CartCreateCommand command = CartCreateCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(cart -> {
                    CartCreatedHook.runHook(getHookRunner(), cart);
                    return cart;
                }, HttpExecution.defaultContext());
    }

    protected CartCreateCommand buildRequest() {
        final CartDraft cartDraft = CartDraftBuilder.of(currency).build();
        return CartCreateCommand.of(cartDraft);
    }
}
