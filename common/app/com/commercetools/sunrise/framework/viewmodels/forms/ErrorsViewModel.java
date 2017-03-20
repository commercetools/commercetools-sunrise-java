package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class ErrorsViewModel extends ViewModel {

    private List<ErrorViewModel> globalErrors;

    public ErrorsViewModel() {
    }

    public List<ErrorViewModel> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(final List<ErrorViewModel> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
