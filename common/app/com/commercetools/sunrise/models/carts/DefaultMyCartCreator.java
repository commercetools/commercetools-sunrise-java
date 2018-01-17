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

final class DefaultMyCartCreator extends AbstractMyCartCreator {

    private final CountryCode country;
    private final CurrencyUnit currency;
    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyCartCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCartInCache myCartInCache,
                         final CountryCode country, final CurrencyUnit currency, final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner, myCartInCache);
        this.country = country;
        this.currency = currency;
        this.myCustomerInSession = myCustomerInSession;
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
