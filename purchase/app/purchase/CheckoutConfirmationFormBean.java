package purchase;

import common.pages.SelectableData;

public class CheckoutConfirmationFormBean {
    private String actionUrl;
    private SelectableData newsletter;
    private SelectableData termsConditions;
    private SelectableData remember;

    public CheckoutConfirmationFormBean() {
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(final String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public SelectableData getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(final SelectableData newsletter) {
        this.newsletter = newsletter;
    }

    public SelectableData getRemember() {
        return remember;
    }

    public void setRemember(final SelectableData remember) {
        this.remember = remember;
    }

    public SelectableData getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(final SelectableData termsConditions) {
        this.termsConditions = termsConditions;
    }
}
