package shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.errors.ErrorsBean;
import io.sphere.sdk.models.Base;

public class CheckoutConfirmationFormBean extends Base {

    private boolean agreeToTerms;
    private ErrorsBean errors;

    public CheckoutConfirmationFormBean() {
    }

    public CheckoutConfirmationFormBean(final boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(final boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
