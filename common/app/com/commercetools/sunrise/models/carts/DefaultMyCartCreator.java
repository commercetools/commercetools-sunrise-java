package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import javax.money.CurrencyUnit;

public final class DefaultMyCartCreator extends AbstractMyCartCreator {

    private final CurrencyUnit currency;
    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyCartCreator(final HookRunner hookRunner, final SphereClient sphereClient,
                         final MyCart myCart, final CurrencyUnit currency,
                         final MyCustomerInSession myCustomerInSession) {
        super(hookRunner, sphereClient, myCart);
        this.currency = currency;
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    protected CartCreateCommand buildRequest() {
        final CartDraft draft = CartDraftBuilder.of(currency)
                .customerId(myCustomerInSession.findId().orElse(null))
                .customerEmail(myCustomerInSession.findEmail().orElse(null))
                .build();
        return CartCreateCommand.of(draft);
    }
}
