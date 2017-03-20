package com.commercetools.sunrise.sessions.country;

import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps the current country in session.
 */
@ImplementedBy(DefaultCountryInSession.class)
public interface CountryInSession extends ResourceStoringOperations<CountryCode> {

    Optional<CountryCode> findCountry();

    @Override
    void store(@Nullable final CountryCode countryCode);

    @Override
    void remove();
}
