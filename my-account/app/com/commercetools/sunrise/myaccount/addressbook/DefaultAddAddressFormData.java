package com.commercetools.sunrise.myaccount.addressbook;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultAddAddressFormData extends Base implements AddAddressFormData {

    private String title;

    @Constraints.Required
    private String firstName;

    @Constraints.Required
    private String lastName;

    @Constraints.Required
    private String streetName;

    private String additionalStreetInfo;

    @Constraints.Required
    private String city;

    @Constraints.Required
    private String postalCode;

    @Constraints.Required
    private String country;

    private String region;

    private boolean defaultShippingAddress;

    private boolean defaultBillingAddress;

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
