package com.commercetools.sunrise.common.contexts;

import com.google.inject.ImplementedBy;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;

import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A container for all information related to the current user, such as selected country, language or customer group.
 */
@ImplementedBy(UserContextImpl.class)
public interface UserContext {


    /**
     * Country associated to the customer.
     * @return the country code
     */
    CountryCode country();

    /**
     * Locales associated to the customer.
     * @return the list of locales
     */
    List<Locale> locales();

    /**
     * Currency associated to the customer.
     * @return the currency unit
     */
    CurrencyUnit currency();

    /**
     * Customer group associated to the customer.
     * @return the reference of the customer group, or empty if the customer has no customer group associated
     */
    Optional<Reference<CustomerGroup>> customerGroup();

    /**
     * Channel associated to the customer.
     * @return the reference of the channel, or empty if the customer has no channel associated
     */
    Optional<Reference<Channel>> channel();

    default Locale locale() {
        return locales().stream()
                .findFirst()
                .orElseThrow(() -> new NoLocaleFoundException("User does not have any valid locale associated."));
    }

    default String languageTag() {
        return locale().toLanguageTag();
    }
}
