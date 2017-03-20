package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.customers.CustomerInfoViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import play.data.Form;

public class MyPersonalDetailsPageContent extends PageContent {

    private CustomerInfoViewModel customerInfo;
    private Form<?> personalDetailsForm;
    private MyPersonalDetailsFormSettingsViewModel personalDetailsFormSettings;

    public MyPersonalDetailsPageContent() {
    }

    public CustomerInfoViewModel getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(final CustomerInfoViewModel customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Form<?> getPersonalDetailsForm() {
        return personalDetailsForm;
    }

    public void setPersonalDetailsForm(final Form<?> personalDetailsForm) {
        this.personalDetailsForm = personalDetailsForm;
    }

    public MyPersonalDetailsFormSettingsViewModel getPersonalDetailsFormSettings() {
        return personalDetailsFormSettings;
    }

    public void setPersonalDetailsFormSettings(final MyPersonalDetailsFormSettingsViewModel personalDetailsFormSettings) {
        this.personalDetailsFormSettings = personalDetailsFormSettings;
    }
}
