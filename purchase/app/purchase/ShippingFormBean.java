package purchase;

public class ShippingFormBean {
    private String actionUrl;
    private String csrfToken;
    private boolean billingAddressDifferentToBillingAddress;

    public ShippingFormBean(final String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(final String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public boolean isBillingAddressDifferentToBillingAddress() {
        return billingAddressDifferentToBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.billingAddressDifferentToBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }
}
