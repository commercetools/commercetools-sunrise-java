package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultAddAddressFormData extends Base implements AddAddressFormData {

    private String title;
    @Required
    private String firstName;
    @Required
    private String lastName;
    @Required
    private String streetName;
    private String additionalStreetInfo;
    @Required
    private String city;
    @Required
    private String postalCode;
    @Required
    private String country;
    private String region;
    private boolean defaultShippingAddress;
    private boolean defaultBillingAddress;

    public String validate() {
        final CountryCode country = CountryCode.getByCode(this.country);
        if (country == null || country.equals(CountryCode.UNDEFINED)) {
            return "Invalid country"; // TODO use i18n version
        }
        return null;
    }

    @Override
    public Address address() {
        final CountryCode countryCode = CountryCode.getByCode(country);
        return AddressBuilder.of(countryCode)
                .title(title)
                .firstName(firstName)
                .lastName(lastName)
                .streetName(streetName)
                .additionalStreetInfo(additionalStreetInfo)
                .city(city)
                .postalCode(postalCode)
                .region(region)
                .build();
    }

    @Override
    public boolean defaultShippingAddress() {
        return defaultShippingAddress;
    }

    @Override
    public boolean defaultBillingAddress() {
        return defaultBillingAddress;
    }
}
