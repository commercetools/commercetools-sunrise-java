package shoppingcart.checkout.confirmation;

import common.errors.ErrorsBean;
import common.models.SelectableBean;
import io.sphere.sdk.models.Base;

public class CheckoutConfirmationFormBean extends Base {
    private SelectableBean newsletter;
    private SelectableBean termsConditions;
    private SelectableBean remember;
    private ErrorsBean errors;

    public CheckoutConfirmationFormBean() {
    }

    public SelectableBean getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(final SelectableBean newsletter) {
        this.newsletter = newsletter;
    }

    public SelectableBean getRemember() {
        return remember;
    }

    public void setRemember(final SelectableBean remember) {
        this.remember = remember;
    }

    public SelectableBean getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(final SelectableBean termsConditions) {
        this.termsConditions = termsConditions;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
