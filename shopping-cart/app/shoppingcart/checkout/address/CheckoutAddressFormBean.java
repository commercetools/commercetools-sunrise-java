package shoppingcart.checkout.address;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;
import play.Configuration;
import shoppingcart.ErrorsBean;

import java.util.Optional;

public class CheckoutAddressFormBean extends Base {
    private boolean billingAddressDifferentToBillingAddress;
    private AddressFormBean shippingAddress;
    private AddressFormBean billingAddress;
    private ErrorsBean errors;

    public CheckoutAddressFormBean() {
    }

    public CheckoutAddressFormBean(final Cart cart, final UserContext userContext, final ProjectContext projectContext,
                                   final I18nResolver i18nResolver, final Configuration configuration) {
        final boolean billingAddressDifferentToBillingAddress = Optional.ofNullable(cart.getBillingAddress())
                .map(Address::getLastName)
                .map(lastName -> lastName != null)
                .orElse(false);
        setBillingAddressDifferentToBillingAddress(billingAddressDifferentToBillingAddress);
        setShippingAddress(new AddressFormBean(cart.getShippingAddress(), userContext, i18nResolver, configuration));
        setBillingAddress(new AddressFormBean(cart.getBillingAddress(), userContext, projectContext, i18nResolver, configuration));
    }

    public CheckoutAddressFormBean(final CheckoutAddressFormData checkoutAddressFormData, final UserContext userContext,
                                   final ProjectContext projectContext, final I18nResolver i18nResolver, final Configuration configuration) {
        setBillingAddressDifferentToBillingAddress(checkoutAddressFormData.isBillingAddressDifferentToBillingAddress());

        final AddressFormBean shippingAddress = createShippingAddressFormBean(checkoutAddressFormData, userContext, projectContext, i18nResolver, configuration);
        setShippingAddress(shippingAddress);

        final AddressFormBean billingAddress = createBillingAddressFormBean(checkoutAddressFormData, userContext, projectContext, i18nResolver, configuration);
        setBillingAddress(billingAddress);
    }

    private AddressFormBean createShippingAddressFormBean(final CheckoutAddressFormData checkoutAddressFormData,
                                                          final UserContext userContext, final ProjectContext projectContext,
                                                          final I18nResolver i18nResolver, final Configuration configuration) {
        final AddressFormBean shippingAddress = new AddressFormBean();
        shippingAddress.setSalutations(new SalutationsFieldsBean(checkoutAddressFormData.getTitleShipping(), userContext, i18nResolver, configuration));
        shippingAddress.setCountries(new CountriesFieldsBean(checkoutAddressFormData.getCountryShipping(), userContext));
        shippingAddress.setFirstName(checkoutAddressFormData.getFirstNameShipping());
        shippingAddress.setLastName(checkoutAddressFormData.getLastNameShipping());
        shippingAddress.setStreetName(checkoutAddressFormData.getStreetNameShipping());
        shippingAddress.setStreetNumber(checkoutAddressFormData.getStreetNameShipping());
        shippingAddress.setAdditionalStreetInfo(checkoutAddressFormData.getAdditionalStreetInfoShipping());
        shippingAddress.setCity(checkoutAddressFormData.getCityShipping());
        shippingAddress.setRegion(checkoutAddressFormData.getRegionShipping());
        shippingAddress.setPostalCode(checkoutAddressFormData.getPostalCodeShipping());
        shippingAddress.setPhone(checkoutAddressFormData.getPhoneShipping());
        shippingAddress.setEmail(checkoutAddressFormData.getEmailShipping());
        return shippingAddress;
    }

    private AddressFormBean createBillingAddressFormBean(final CheckoutAddressFormData checkoutAddressFormData,
                                                         final UserContext userContext, final ProjectContext projectContext,
                                                         final I18nResolver i18nResolver, final Configuration configuration) {
        final AddressFormBean billingAddress = new AddressFormBean();
        billingAddress.setSalutations(new SalutationsFieldsBean(checkoutAddressFormData.getTitleBilling(), userContext, i18nResolver, configuration));
        billingAddress.setCountries(new CountriesFieldsBean(checkoutAddressFormData.getCountryBilling(), userContext, projectContext));
        billingAddress.setFirstName(checkoutAddressFormData.getFirstNameBilling());
        billingAddress.setLastName(checkoutAddressFormData.getLastNameBilling());
        billingAddress.setStreetName(checkoutAddressFormData.getStreetNameBilling());
        billingAddress.setStreetNumber(checkoutAddressFormData.getStreetNameBilling());
        billingAddress.setAdditionalStreetInfo(checkoutAddressFormData.getAdditionalStreetInfoBilling());
        billingAddress.setCity(checkoutAddressFormData.getCityBilling());
        billingAddress.setRegion(checkoutAddressFormData.getRegionBilling());
        billingAddress.setPostalCode(checkoutAddressFormData.getPostalCodeBilling());
        billingAddress.setPhone(checkoutAddressFormData.getPhoneBilling());
        billingAddress.setEmail(checkoutAddressFormData.getEmailBilling());
        return billingAddress;
    }

    public AddressFormBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressFormBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public boolean isBillingAddressDifferentToBillingAddress() {
        return billingAddressDifferentToBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.billingAddressDifferentToBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public AddressFormBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressFormBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
