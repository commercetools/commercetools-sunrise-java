package purchase;

import play.data.Form;

import java.util.Collections;
import java.util.List;

public class ErrorsBean {
    private List<ErrorBean> globalErrors;

    public ErrorsBean() {
    }

    public ErrorsBean(final Form<CheckoutShippingFormData> filledForm) {
        setGlobalErrors(Collections.singletonList(new ErrorBean(filledForm.errorsAsJson().toString())));
    }

    public List<ErrorBean> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(final List<ErrorBean> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
