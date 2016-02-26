package common.errors;

import io.sphere.sdk.models.Base;
import play.data.Form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorsBean extends Base {
    private List<ErrorBean> globalErrors;

    public ErrorsBean() {
    }

    public ErrorsBean(final String errorMessage) {
        this.globalErrors = Collections.singletonList(new ErrorBean(errorMessage));
    }

    public ErrorsBean(final Form<?> filledForm) {
        this.globalErrors = new ArrayList<>();
        filledForm.errors()
                .forEach((field, errors) -> errors
                        .forEach(error -> globalErrors.add(new ErrorBean(field + ": " + error))));
    }

    public List<ErrorBean> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(final List<ErrorBean> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
