package purchase;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;
import play.Configuration;

import java.util.Optional;

public class CheckoutShippingFormBean extends Base {
    private boolean billingAddressDifferentToBillingAddress;
    private AddressFormBean shippingAddress;
    private AddressFormBean billingAddress;
    private ShippingMethodsFormBean shippingMethods;
    private ErrorsBean errors;


    public CheckoutShippingFormBean() {
    }

    public CheckoutShippingFormBean(final Cart cart, final UserContext userContext, final ProjectContext projectContext,
                                    final ShippingMethods shippingMethods, final I18nResolver i18nResolver, final Configuration configuration) {
        final boolean billingAddressDifferentToBillingAddress = Optional.ofNullable(cart.getBillingAddress())
                .map(Address::getLastName)
                .map(lastName -> lastName != null)
                .orElse(false);
        setBillingAddressDifferentToBillingAddress(billingAddressDifferentToBillingAddress);
        setShippingAddress(new AddressFormBean(cart.getShippingAddress(), userContext, projectContext, i18nResolver, configuration));
        setBillingAddress(new AddressFormBean(cart.getBillingAddress(), userContext, projectContext, i18nResolver, configuration));
        setShippingMethods(new ShippingMethodsFormBean(cart, shippingMethods));
    }

    public CheckoutShippingFormBean(final CheckoutShippingFormData checkoutShippingFormData, final UserContext userContext,
                                    final ProjectContext projectContext, final ShippingMethods shippingMethods,
                                    final I18nResolver i18nResolver, final Configuration configuration) {
        setBillingAddressDifferentToBillingAddress(checkoutShippingFormData.isBillingAddressDifferentToBillingAddress());

        final AddressFormBean shippingAddress = createShippingAddressFormBean(checkoutShippingFormData, userContext, projectContext, i18nResolver, configuration);
        setShippingAddress(shippingAddress);

        final AddressFormBean billingAddress = createBillingAddressFormBean(checkoutShippingFormData, userContext, projectContext, i18nResolver, configuration);
        setBillingAddress(billingAddress);

        setShippingMethods(new ShippingMethodsFormBean(checkoutShippingFormData, shippingMethods));
    }

    private AddressFormBean createShippingAddressFormBean(final CheckoutShippingFormData checkoutShippingFormData,
                                                          final UserContext userContext, final ProjectContext projectContext,
                                                          final I18nResolver i18nResolver, final Configuration configuration) {
        final AddressFormBean shippingAddress = new AddressFormBean();
        shippingAddress.setSalutations(new SalutationsFieldsBean(checkoutShippingFormData.getTitleShipping(), userContext, i18nResolver, configuration));
        shippingAddress.setCountries(new CountriesFieldsBean(checkoutShippingFormData.getCountryShipping(), userContext, projectContext));
        shippingAddress.setFirstName(checkoutShippingFormData.getFirstNameShipping());
        shippingAddress.setLastName(checkoutShippingFormData.getLastNameShipping());
        shippingAddress.setStreetName(checkoutShippingFormData.getStreetNameShipping());
        shippingAddress.setStreetNumber(checkoutShippingFormData.getStreetNameShipping());
        shippingAddress.setAdditionalStreetInfo(checkoutShippingFormData.getAdditionalStreetInfoShipping());
        shippingAddress.setCity(checkoutShippingFormData.getCityShipping());
        shippingAddress.setRegion(checkoutShippingFormData.getRegionShipping());
        shippingAddress.setPostalCode(checkoutShippingFormData.getPostalCodeShipping());
        shippingAddress.setPhone(checkoutShippingFormData.getPhoneShipping());
        shippingAddress.setEmail(checkoutShippingFormData.getEmailShipping());
        return shippingAddress;
    }

    private AddressFormBean createBillingAddressFormBean(final CheckoutShippingFormData checkoutShippingFormData,
                                                         final UserContext userContext, final ProjectContext projectContext,
                                                         final I18nResolver i18nResolver, final Configuration configuration) {
        final AddressFormBean billingAddress = new AddressFormBean();
        billingAddress.setSalutations(new SalutationsFieldsBean(checkoutShippingFormData.getTitleBilling(), userContext, i18nResolver, configuration));
        billingAddress.setCountries(new CountriesFieldsBean(checkoutShippingFormData.getCountryBilling(), userContext, projectContext));
        billingAddress.setFirstName(checkoutShippingFormData.getFirstNameBilling());
        billingAddress.setLastName(checkoutShippingFormData.getLastNameBilling());
        billingAddress.setStreetName(checkoutShippingFormData.getStreetNameBilling());
        billingAddress.setStreetNumber(checkoutShippingFormData.getStreetNameBilling());
        billingAddress.setAdditionalStreetInfo(checkoutShippingFormData.getAdditionalStreetInfoBilling());
        billingAddress.setCity(checkoutShippingFormData.getCityBilling());
        billingAddress.setRegion(checkoutShippingFormData.getRegionBilling());
        billingAddress.setPostalCode(checkoutShippingFormData.getPostalCodeBilling());
        billingAddress.setPhone(checkoutShippingFormData.getPhoneBilling());
        billingAddress.setEmail(checkoutShippingFormData.getEmailBilling());
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

    public ShippingMethodsFormBean getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(final ShippingMethodsFormBean shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
