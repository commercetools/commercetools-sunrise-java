package com.commercetools.sunrise.framework.cart.addlineitem;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.concurrent.CompletionStage;

public class DefaultCartCreator extends AbstractCartCreateExecutor implements CartCreator {

    private final CountryCode country;
    private final CurrencyUnit currency;
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCartCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                                 final CountryCode country, final CurrencyUnit currency,
                                 final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.country = country;
        this.currency = currency;
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<Cart> get() {
        return executeRequest(buildRequest());
    }

    protected CartCreateCommand buildRequest() {
        final CartDraft cartDraft = CartDraftBuilder.of(currency)
                .country(country)
                .shippingAddress(Address.of(country))
                .customerId(customerInSession.findCustomerId().orElse(null))
                .customerEmail(customerInSession.findCustomerEmail().orElse(null))
                .build();
        return CartCreateCommand.of(cartDraft);
    }
}
