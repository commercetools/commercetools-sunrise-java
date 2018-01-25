package com.commercetools.sunrise.myaccount.addressbook;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultAddAddressFormData extends Base implements AddAddressFormData {

    public String title;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String streetName;

    public String additionalStreetInfo;

    @Constraints.Required
    public String city;

    @Constraints.Required
    public String postalCode;

    @Constraints.Required
    public String country;

    public String region;

    public boolean defaultShippingAddress;

    public boolean defaultBillingAddress;

    @Override
    public AddAddress addAddress() {
        final CountryCode countryCode = CountryCode.getByCode(country);
        final Address address = AddressBuilder.of(countryCode)
                .title(title)
                .firstName(firstName)
                .lastName(lastName)
                .streetName(streetName)
                .additionalStreetInfo(additionalStreetInfo)
                .city(city)
                .postalCode(postalCode)
                .region(region)
                .build();
        return AddAddress.of(address);
    }

    @Override
    public boolean defaultShippingAddress() {
        return defaultShippingAddress;
    }

    @Override
    public boolean defaultBillingAddress() {
        return defaultBillingAddress;
    }

    public String validate() {
        final CountryCode country = CountryCode.getByCode(this.country);
        if (country == null || country.equals(CountryCode.UNDEFINED)) {
            return "errors.invalidCountry";
        }
        return null;
    }
}
