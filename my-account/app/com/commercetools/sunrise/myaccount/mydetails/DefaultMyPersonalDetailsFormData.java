package com.commercetools.sunrise.myaccount.mydetails;

import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultMyPersonalDetailsFormData extends Base implements MyPersonalDetailsFormData {

    private String title;
    @Required
    private String firstName;
    @Required
    private String lastName;
    @Required
    private String email;

    @Override
    public CustomerName customerName() {
        return CustomerName.ofTitleFirstAndLastName(title, firstName, lastName);
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public void applyCustomerName(final CustomerName customerName) {
        this.title = customerName.getTitle();
        this.firstName = customerName.getFirstName();
        this.lastName = customerName.getLastName();
    }

    @Override
    public void applyEmail(final String email) {
        this.email = email;
    }


    // Getters & setters

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
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

