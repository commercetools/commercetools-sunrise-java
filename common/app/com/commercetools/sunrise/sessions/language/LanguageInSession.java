package com.commercetools.sunrise.sessions.language;

import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;

/**
 * Keeps the current language in session.
 */
@ImplementedBy(DefaultLanguageInSession.class)
public interface LanguageInSession extends ResourceStoringOperations<Locale> {

    Optional<Locale> findCountry();

    @Override
    void store(@Nullable final Locale locale);

    @Override
    void remove();
}
