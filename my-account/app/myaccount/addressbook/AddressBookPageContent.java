package myaccount.addressbook;

import common.controllers.PageContent;
import myaccount.mydetails.CustomerBean;
import myaccount.mydetails.MyPersonalDetailsFormBean;

public class AddressBookPageContent extends PageContent {

    private CustomerBean customer;
    private MyPersonalDetailsFormBean personalDetailsForm;

    public AddressBookPageContent() {
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
