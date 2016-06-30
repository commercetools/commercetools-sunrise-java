package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.forms.FormBean;
import com.commercetools.sunrise.common.models.TitleFormFieldBean;

public class MyPersonalDetailsFormBean extends FormBean {

    private TitleFormFieldBean salutations;
    private String firstName;
    private String lastName;
    private String email;

    public TitleFormFieldBean getSalutations() {
        return salutations;
    }

    public void setSalutations(final TitleFormFieldBean salutations) {
        this.salutations = salutations;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
