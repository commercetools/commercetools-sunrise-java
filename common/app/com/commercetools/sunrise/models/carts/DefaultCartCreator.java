package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCartCreator extends AbstractCartCreator {

    private final CountryCode country;
    private final CurrencyUnit currency;
    private final MyCustomerInSession myCustomerInSession;

    @Inject
    protected DefaultCartCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                                 final CountryCode country, final CurrencyUnit currency, final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner);
        this.country = country;
        this.currency = currency;
        this.myCustomerInSession = myCustomerInSession;
    }

    protected final CountryCode getCountry() {
        return country;
    }

    protected final CurrencyUnit getCurrency() {
        return currency;
    }

    protected final MyCustomerInSession getMyCustomerInSession() {
        return myCustomerInSession;
    }

    @Override
    public CompletionStage<CartDraft> defaultDraft() {
        return completedFuture(CartDraftBuilder.of(currency)
                .country(country)
                .shippingAddress(Address.of(country))
                .customerId(myCustomerInSession.findId().orElse(null))
                .customerEmail(myCustomerInSession.findEmail().orElse(null))
                .build());
    }
}
