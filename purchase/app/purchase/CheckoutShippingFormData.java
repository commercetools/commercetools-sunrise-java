package purchase;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutShippingFormData extends Base {
    public interface Validation {}

    private String csrfToken;

    private boolean billingAddressDifferentToBillingAddress;

    @Constraints.Required(groups = Validation.class)
    private String shippingMethodId;

    private String additionalStreetInfoShipping;
    private String cityShipping;
    @Constraints.Required(groups = Validation.class)
    private String countryShipping;
    private String emailShipping;
    private String firstNameShipping;
    @Constraints.Required(groups = Validation.class)
    private String lastNameShipping;
    private String phoneShipping;
    private String postalCodeShipping;
    private String regionShipping;
    private String streetNameShipping;
    private String titleShipping;

    private String additionalStreetInfoBilling;
    private String cityBilling;
    private String countryBilling;
    private String emailBilling;
    private String firstNameBilling;
    private String lastNameBilling;
    private String phoneBilling;
    private String postalCodeBilling;
    private String regionBilling;
    private String streetNameBilling;
    private String titleBilling;

    public CheckoutShippingFormData() {
    }

    public String getAdditionalStreetInfoBilling() {
        return additionalStreetInfoBilling;
    }

    public void setAdditionalStreetInfoBilling(final String additionalStreetInfoBilling) {
        this.additionalStreetInfoBilling = additionalStreetInfoBilling;
    }

    public String getAdditionalStreetInfoShipping() {
        return additionalStreetInfoShipping;
    }

    public void setAdditionalStreetInfoShipping(final String additionalStreetInfoShipping) {
        this.additionalStreetInfoShipping = additionalStreetInfoShipping;
    }

    public String getCityBilling() {
        return cityBilling;
    }

    public void setCityBilling(final String cityBilling) {
        this.cityBilling = cityBilling;
    }

    public String getCityShipping() {
        return cityShipping;
    }

    public void setCityShipping(final String cityShipping) {
        this.cityShipping = cityShipping;
    }

    public String getCountryBilling() {
        return countryBilling;
    }

    public void setCountryBilling(final String countryBilling) {
        this.countryBilling = countryBilling;
    }

    public String getCountryShipping() {
        return countryShipping;
    }

    public void setCountryShipping(final String countryShipping) {
        this.countryShipping = countryShipping;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getEmailBilling() {
        return emailBilling;
    }

    public void setEmailBilling(final String emailBilling) {
        this.emailBilling = emailBilling;
    }

    public String getEmailShipping() {
        return emailShipping;
    }

    public void setEmailShipping(final String emailShipping) {
        this.emailShipping = emailShipping;
    }

    public String getFirstNameBilling() {
        return firstNameBilling;
    }

    public void setFirstNameBilling(final String firstNameBilling) {
        this.firstNameBilling = firstNameBilling;
    }

    public String getFirstNameShipping() {
        return firstNameShipping;
    }

    public void setFirstNameShipping(final String firstNameShipping) {
        this.firstNameShipping = firstNameShipping;
    }

    public String getLastNameBilling() {
        return lastNameBilling;
    }

    public void setLastNameBilling(final String lastNameBilling) {
        this.lastNameBilling = lastNameBilling;
    }

    public String getLastNameShipping() {
        return lastNameShipping;
    }

    public void setLastNameShipping(final String lastNameShipping) {
        this.lastNameShipping = lastNameShipping;
    }

    public String getPhoneBilling() {
        return phoneBilling;
    }

    public void setPhoneBilling(final String phoneBilling) {
        this.phoneBilling = phoneBilling;
    }

    public String getPhoneShipping() {
        return phoneShipping;
    }

    public void setPhoneShipping(final String phoneShipping) {
        this.phoneShipping = phoneShipping;
    }

    public String getPostalCodeBilling() {
        return postalCodeBilling;
    }

    public void setPostalCodeBilling(final String postalCodeBilling) {
        this.postalCodeBilling = postalCodeBilling;
    }

    public String getPostalCodeShipping() {
        return postalCodeShipping;
    }

    public void setPostalCodeShipping(final String postalCodeShipping) {
        this.postalCodeShipping = postalCodeShipping;
    }

    public String getRegionBilling() {
        return regionBilling;
    }

    public void setRegionBilling(final String regionBilling) {
        this.regionBilling = regionBilling;
    }

    public String getRegionShipping() {
        return regionShipping;
    }

    public void setRegionShipping(final String regionShipping) {
        this.regionShipping = regionShipping;
    }

    public String getStreetNameBilling() {
        return streetNameBilling;
    }

    public void setStreetNameBilling(final String streetNameBilling) {
        this.streetNameBilling = streetNameBilling;
    }

    public String getStreetNameShipping() {
        return streetNameShipping;
    }

    public void setStreetNameShipping(final String streetNameShipping) {
        this.streetNameShipping = streetNameShipping;
    }

    public boolean isBillingAddressDifferentToBillingAddress() {
        return billingAddressDifferentToBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.billingAddressDifferentToBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public String getTitleBilling() {
        return titleBilling;
    }

    public void setTitleBilling(final String titleBilling) {
        this.titleBilling = titleBilling;
    }

    public String getTitleShipping() {
        return titleShipping;
    }

    public void setTitleShipping(final String titleShipping) {
        this.titleShipping = titleShipping;
    }

    public String getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(final String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }
}
