package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.PageContent;
import play.data.Form;

public class MyPersonalDetailsPageContent extends PageContent {

    private CustomerInfoBean customerInfo;
    private Form<?> personalDetailsForm;
    private MyPersonalDetailsFormSettingsBean personalDetailsFormSettings;

    public MyPersonalDetailsPageContent() {
    }

    public CustomerInfoBean getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(final CustomerInfoBean customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Form<?> getPersonalDetailsForm() {
        return personalDetailsForm;
    }

    public void setPersonalDetailsForm(final Form<?> personalDetailsForm) {
        this.personalDetailsForm = personalDetailsForm;
    }

    public MyPersonalDetailsFormSettingsBean getPersonalDetailsFormSettings() {
        return personalDetailsFormSettings;
    }

    public void setPersonalDetailsFormSettings(final MyPersonalDetailsFormSettingsBean personalDetailsFormSettings) {
        this.personalDetailsFormSettings = personalDetailsFormSettings;
    }
}
