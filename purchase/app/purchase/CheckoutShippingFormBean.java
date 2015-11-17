package purchase;

import common.contexts.UserContext;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.i18n.Messages;

import java.util.Optional;

public class CheckoutShippingFormBean {
    private String actionUrl;
    private String csrfToken;
    private boolean billingAddressDifferentToBillingAddress;
    private AddressFormBean shippingAddress;
    private AddressFormBean billingAddress;
    private ShippingMethodsFormBean shippingMethods;


    public CheckoutShippingFormBean() {
    }

    public CheckoutShippingFormBean(final Cart cart, final ReverseRouter reverseRouter, final String csrfToken, final UserContext userContext, final ShippingMethods shippingMethods, final Messages messages, final Configuration configuration) {
        setActionUrl(reverseRouter.processCheckoutShippingForm(userContext.locale().getLanguage()).url());
        setCsrfToken(csrfToken);
        final boolean billingAddressDifferentToBillingAddress = Optional.ofNullable(cart.getBillingAddress())
                .map(Address::getLastName)
                .map(lastName -> lastName != null)
                .orElse(false);
        setBillingAddressDifferentToBillingAddress(billingAddressDifferentToBillingAddress);
        setShippingAddress(new AddressFormBean(cart.getShippingAddress(), userContext, messages, configuration));
        setBillingAddress(new AddressFormBean(cart.getBillingAddress(), userContext, messages, configuration));
        setShippingMethods(new ShippingMethodsFormBean(cart, shippingMethods));
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(final String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
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
}
