package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutBillingAddressFormData extends Base implements WithAddressExport {

    private String csrfToken;

    private String titleBilling;
    @Constraints.Required
    private String firstNameBilling;
    @Constraints.Required
    private String lastNameBilling;
    @Constraints.Required
    private String streetNameBilling;
    private String additionalStreetInfoBilling;
    @Constraints.Required
    private String cityBilling;
    @Constraints.Required
    private String postalCodeBilling;
    @Constraints.Required
    private String countryBilling;
    private String regionBilling;
    private String phoneBilling;
    @Constraints.Required
    private String emailBilling;

    public String validate() {
        final CountryCode country = CountryCode.getByCode(countryBilling);
        if (country == null || country.equals(CountryCode.UNDEFINED)) {
            return "Invalid country"; // TODO use i18n version
        }
        return null;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getTitleBilling() {
        return titleBilling;
    }

    public void setTitleBilling(final String titleBilling) {
        this.titleBilling = titleBilling;
    }

    public String getFirstNameBilling() {
        return firstNameBilling;
    }

    public void setFirstNameBilling(final String firstNameBilling) {
        this.firstNameBilling = firstNameBilling;
    }

    public String getLastNameBilling() {
        return lastNameBilling;
    }

    public void setLastNameBilling(final String lastNameBilling) {
        this.lastNameBilling = lastNameBilling;
    }

    public String getStreetNameBilling() {
        return streetNameBilling;
    }

    public void setStreetNameBilling(final String streetNameBilling) {
        this.streetNameBilling = streetNameBilling;
    }

    public String getAdditionalStreetInfoBilling() {
        return additionalStreetInfoBilling;
    }

    public void setAdditionalStreetInfoBilling(final String additionalStreetInfoBilling) {
        this.additionalStreetInfoBilling = additionalStreetInfoBilling;
    }

    public String getCityBilling() {
        return cityBilling;
    }

    public void setCityBilling(final String cityBilling) {
        this.cityBilling = cityBilling;
    }

    public String getPostalCodeBilling() {
        return postalCodeBilling;
    }

    public void setPostalCodeBilling(final String postalCodeBilling) {
        this.postalCodeBilling = postalCodeBilling;
    }

    public String getCountryBilling() {
        return countryBilling;
    }

    public void setCountryBilling(final String countryBilling) {
        this.countryBilling = countryBilling;
    }

    public String getRegionBilling() {
        return regionBilling;
    }

    public void setRegionBilling(final String regionBilling) {
        this.regionBilling = regionBilling;
    }

    public String getPhoneBilling() {
        return phoneBilling;
    }

    public void setPhoneBilling(final String phoneBilling) {
        this.phoneBilling = phoneBilling;
    }

    public String getEmailBilling() {
        return emailBilling;
    }

    public void setEmailBilling(final String emailBilling) {
        this.emailBilling = emailBilling;
    }

    public Address toAddress() {
        final CountryCode country = CountryCode.getByCode(countryBilling);
        return AddressBuilder.of(country)
                .title(titleBilling)
                .firstName(firstNameBilling)
                .lastName(lastNameBilling)
                .streetName(streetNameBilling)
                .additionalStreetInfo(additionalStreetInfoBilling)
                .city(cityBilling)
                .postalCode(postalCodeBilling)
                .region(regionBilling)
                .phone(phoneBilling)
                .email(emailBilling)
                .build();
    }
}
