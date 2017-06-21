package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

import javax.annotation.Nullable;
import java.util.Optional;

public class DefaultCheckoutAddressFormData extends Base implements CheckoutAddressFormData {

    private String titleShipping;
    @Required
    @MinLength(1)
    private String firstNameShipping;
    @Required
    @MinLength(1)
    private String lastNameShipping;
    @Required
    @MinLength(1)
    private String streetNameShipping;
    private String additionalStreetInfoShipping;
    @Required
    @MinLength(1)
    private String cityShipping;
    @Required
    @MinLength(1)
    private String postalCodeShipping;
    @Required
    @MinLength(1)
    private String countryShipping;
    private String regionShipping;
    private String phoneShipping;
    @Required
    @MinLength(1)
    @Email
    private String emailShipping;

    private String titleBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String firstNameBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String lastNameBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String streetNameBilling;
    private String additionalStreetInfoBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String cityBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String postalCodeBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String countryBilling;
    private String regionBilling;
    private String phoneBilling;
    @MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Email(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String emailBilling;

    private boolean billingAddressDifferentToBillingAddress;

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

    @Override
    public Address shippingAddress() {
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

    @Override
    @Nullable
    public Address billingAddress() {
        if (billingAddressDifferentToBillingAddress) {
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
    public void applyCart(final Cart cart) {
        Optional.ofNullable(cart.getShippingAddress()).ifPresent(this::applyShippingAddress);
        Optional.ofNullable(cart.getBillingAddress()).ifPresent(this::applyBillingAddress);
        billingAddressDifferentToBillingAddress = cart.getBillingAddress() != null;
    }

    private void applyShippingAddress(final Address address) {
        this.titleShipping = address.getTitle();
        this.firstNameShipping = address.getFirstName();
        this.lastNameShipping = address.getLastName();
        this.streetNameShipping = address.getStreetName();
        this.additionalStreetInfoShipping = address.getAdditionalStreetInfo();
        this.cityShipping = address.getCity();
        this.postalCodeShipping = address.getPostalCode();
        this.countryShipping = address.getCountry().getAlpha2();
        this.regionShipping = address.getRegion();
        this.phoneShipping = address.getPhone();
        this.emailShipping = address.getEmail();
    }

    private void applyBillingAddress(final Address address) {
        this.titleBilling = address.getTitle();
        this.firstNameBilling = address.getFirstName();
        this.lastNameBilling = address.getLastName();
        this.streetNameBilling = address.getStreetName();
        this.additionalStreetInfoBilling = address.getAdditionalStreetInfo();
        this.cityBilling = address.getCity();
        this.postalCodeBilling = address.getPostalCode();
        this.countryBilling = address.getCountry().getAlpha2();
        this.regionBilling = address.getRegion();
        this.phoneBilling = address.getPhone();
        this.emailBilling = address.getEmail();
    }


    // Getters & setters

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

    public boolean isBillingAddressDifferentToBillingAddress() {
        return billingAddressDifferentToBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.billingAddressDifferentToBillingAddress = billingAddressDifferentToBillingAddress;
    }
}
