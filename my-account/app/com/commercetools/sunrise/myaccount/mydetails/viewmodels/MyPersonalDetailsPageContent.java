package com.commercetools.sunrise.myaccount.mydetails.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

public class MyPersonalDetailsPageContent extends PageContent {

    private Customer customer;
    private Form<?> personalDetailsForm;
    private MyPersonalDetailsFormSettingsViewModel personalDetailsFormSettings;

    public MyPersonalDetailsPageContent() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customerInfo) {
        this.customer = customerInfo;
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
