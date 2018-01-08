package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

public class DefaultCartCreator extends AbstractCartCreator implements CartCreator {

    private final CountryCode country;
    private final CurrencyUnit currency;
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCartCreator(final SphereClient sphereClient, final HookRunner hookRunner,
                                 final CountryCode country, final CurrencyUnit currency, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.country = country;
        this.currency = currency;
        this.customerInSession = customerInSession;
    }

    protected final CountryCode getCountry() {
        return country;
    }

    protected final CurrencyUnit getCurrency() {
        return currency;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    @Override
    protected CartCreateCommand buildRequest(@Nullable final CartDraft template) {
        return CartCreateCommand.of(buildDraft(template));
    }

    private CartDraft buildDraft(@Nullable final CartDraft template) {
        final CartDraftBuilder builder = template != null ? CartDraftBuilder.of(template) : CartDraftBuilder.of(currency);
        return builder
                .country(country)
                .shippingAddress(Address.of(country))
                .customerId(customerInSession.findId().orElse(null))
                .customerEmail(customerInSession.findEmail().orElse(null))
                .build();
    }
}
