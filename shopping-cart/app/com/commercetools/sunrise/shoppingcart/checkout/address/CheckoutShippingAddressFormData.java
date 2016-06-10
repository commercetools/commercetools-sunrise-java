package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutShippingAddressFormData extends Base {

    private String csrfToken;

    private boolean billingAddressDifferentToBillingAddress;

    private String titleShipping;
    @Constraints.Required
    private String firstNameShipping;
    @Constraints.Required
    private String lastNameShipping;
    @Constraints.Required
    private String streetNameShipping;
    private String additionalStreetInfoShipping;
    @Constraints.Required
    private String cityShipping;
    @Constraints.Required
    private String postalCodeShipping;
    @Constraints.Required
    private String countryShipping;
    private String regionShipping;
    private String phoneShipping;
    @Constraints.Required
    private String emailShipping;

    public String validate() {
        final CountryCode country = CountryCode.getByCode(countryShipping);
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

    public boolean isBillingAddressDifferentToBillingAddress() {
        return billingAddressDifferentToBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.billingAddressDifferentToBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public String getTitleShipping() {
        return titleShipping;
    }

    public void setTitleShipping(final String titleShipping) {
        this.titleShipping = titleShipping;
    }

    public String getFirstNameShipping() {
        return firstNameShipping;
    }

    public void setFirstNameShipping(final String firstNameShipping) {
        this.firstNameShipping = firstNameShipping;
    }

    public String getLastNameShipping() {
        return lastNameShipping;
    }

    public void setLastNameShipping(final String lastNameShipping) {
        this.lastNameShipping = lastNameShipping;
    }

    public String getStreetNameShipping() {
        return streetNameShipping;
    }

    public void setStreetNameShipping(final String streetNameShipping) {
        this.streetNameShipping = streetNameShipping;
    }

    public String getAdditionalStreetInfoShipping() {
        return additionalStreetInfoShipping;
    }

    public void setAdditionalStreetInfoShipping(final String additionalStreetInfoShipping) {
        this.additionalStreetInfoShipping = additionalStreetInfoShipping;
    }

    public String getCityShipping() {
        return cityShipping;
    }

    public void setCityShipping(final String cityShipping) {
        this.cityShipping = cityShipping;
    }

    public String getPostalCodeShipping() {
        return postalCodeShipping;
    }

    public void setPostalCodeShipping(final String postalCodeShipping) {
        this.postalCodeShipping = postalCodeShipping;
    }

    public String getCountryShipping() {
        return countryShipping;
    }

    public void setCountryShipping(final String countryShipping) {
        this.countryShipping = countryShipping;
    }

    public String getRegionShipping() {
        return regionShipping;
    }

    public void setRegionShipping(final String regionShipping) {
        this.regionShipping = regionShipping;
    }

    public String getPhoneShipping() {
        return phoneShipping;
    }

    public void setPhoneShipping(final String phoneShipping) {
        this.phoneShipping = phoneShipping;
    }

    public String getEmailShipping() {
        return emailShipping;
    }

    public void setEmailShipping(final String emailShipping) {
        this.emailShipping = emailShipping;
    }

    public Address toAddress() {
        final CountryCode country = CountryCode.getByCode(countryShipping);
        return AddressBuilder.of(country)
                .title(titleShipping)
                .firstName(firstNameShipping)
                .lastName(lastNameShipping)
                .streetName(streetNameShipping)
                .additionalStreetInfo(additionalStreetInfoShipping)
                .city(cityShipping)
                .postalCode(postalCodeShipping)
                .region(regionShipping)
                .phone(phoneShipping)
                .email(emailShipping)
                .build();
    }
}
