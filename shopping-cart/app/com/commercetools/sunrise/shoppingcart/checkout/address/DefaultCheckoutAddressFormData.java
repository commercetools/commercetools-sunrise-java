package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

import javax.annotation.Nullable;

public class DefaultCheckoutAddressFormData extends Base implements CheckoutAddressFormData {

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
    private String titleBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String firstNameBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String lastNameBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String streetNameBilling;
    private String additionalStreetInfoBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String cityBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String postalCodeBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String countryBilling;
    private String regionBilling;
    private String phoneBilling;
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String emailBilling;

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

    @Override
    @Nullable
    public Address getBillingAddress() {
        if (isBillingAddressDifferentToBillingAddress()) {
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
        } else {
            return null;
        }
    }

    @Override
    public Address getShippingAddress() {
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

    public String validate() {
        final CountryCode shippingCountry = CountryCode.getByCode(countryShipping);
        if (shippingCountry == null || shippingCountry.equals(CountryCode.UNDEFINED)) {
            return "Invalid shipping country"; // TODO use i18n version
        }
        if (billingAddressDifferentToBillingAddress) {
            final CountryCode billingCountry = CountryCode.getByCode(countryBilling);
            if (billingCountry == null || billingCountry.equals(CountryCode.UNDEFINED)) {
                return "Invalid billing country"; // TODO use i18n version
            }
        }
        return null;
    }
}
