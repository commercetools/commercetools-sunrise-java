package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.PageContent;

public class MyPersonalDetailsPageContent extends PageContent {

    private CustomerInfoBean customerInfo;
    private MyPersonalDetailsFormBean personalDetailsForm;

    public MyPersonalDetailsPageContent() {
    }

    public CustomerInfoBean getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(final CustomerInfoBean customerInfo) {
        this.customerInfo = customerInfo;
    }

    public MyPersonalDetailsFormBean getPersonalDetailsForm() {
        return personalDetailsForm;
    }

    public void setPersonalDetailsForm(final MyPersonalDetailsFormBean personalDetailsForm) {
        this.personalDetailsForm = personalDetailsForm;
    }
}
