package shoppingcart.checkout.confirmation;

import common.errors.ErrorsBean;
import common.models.SelectableData;
import io.sphere.sdk.models.Base;

public class CheckoutConfirmationFormBean extends Base {
    private SelectableData newsletter;
    private SelectableData termsConditions;
    private SelectableData remember;
    private ErrorsBean errors;

    public CheckoutConfirmationFormBean() {
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

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
