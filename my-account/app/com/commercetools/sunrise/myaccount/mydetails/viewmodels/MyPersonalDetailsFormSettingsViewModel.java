package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModel;
import com.commercetools.sunrise.common.models.ViewModel;

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
