package com.commercetools.sunrise.shoppingcart.checkout;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Email;

public class DefaultSetAddressFormData extends Base implements SetAddressFormData {

    private String titleShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String firstNameShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String lastNameShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String streetNameShipping;
    
    private String additionalStreetInfoShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String cityShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String postalCodeShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    private String countryShipping;
    
    private String regionShipping;
    
    private String phoneShipping;
    
    @Constraints.Required
    @Constraints.MinLength(1)
    @Constraints.Email
    private String emailShipping;

    private String titleBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String firstNameBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String lastNameBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String streetNameBilling;
    
    private String additionalStreetInfoBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String cityBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String postalCodeBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String countryBilling;
    
    private String regionBilling;
    
    private String phoneBilling;
    
    @Constraints.MinLength(value = 1, groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Constraints.Required(groups = BillingAddressDifferentToShippingAddressGroup.class)
    @Email(groups = BillingAddressDifferentToShippingAddressGroup.class)
    private String emailBilling;

    private boolean billingAddressDifferentToBillingAddress;

    @Override
    public SetShippingAddress setShippingAddress() {
        final CountryCode country = CountryCode.getByCode(countryShipping);
        final Address address = AddressBuilder.of(country)
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
        return SetShippingAddress.of(address);
    }

    @Override
    public SetBillingAddress setBillingAddress() {
        Address address = null;
        if (billingAddressDifferentToBillingAddress) {
            final CountryCode country = CountryCode.getByCode(countryBilling);
            address = AddressBuilder.of(country)
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
        return SetBillingAddress.of(address);
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
