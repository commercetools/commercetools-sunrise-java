package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.PageContent;

public class MyPersonalDetailsPageContent extends PageContent {

    private CustomerBean customer;
    private MyPersonalDetailsFormBean personalDetailsForm;

    public MyPersonalDetailsPageContent() {
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(final CustomerBean customer) {
        this.customer = customer;
    }

    public MyPersonalDetailsFormBean getPersonalDetailsForm() {
        return personalDetailsForm;
    }

    public void setPersonalDetailsForm(final MyPersonalDetailsFormBean personalDetailsForm) {
        this.personalDetailsForm = personalDetailsForm;
    }
}
