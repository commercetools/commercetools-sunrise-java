package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.titles.TitleFormFieldViewModel;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class MyPersonalDetailsFormSettingsViewModel extends ViewModel {

    private TitleFormFieldViewModel title;

    public MyPersonalDetailsFormSettingsViewModel() {
    }

    public TitleFormFieldViewModel getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldViewModel title) {
        this.title = title;
    }
}
