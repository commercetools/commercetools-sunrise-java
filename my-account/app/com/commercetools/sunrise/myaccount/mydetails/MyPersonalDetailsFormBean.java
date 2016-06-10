package com.commercetools.sunrise.myaccount.mydetails;

import common.contexts.UserContext;
import common.errors.ErrorsBean;
import common.models.TitleFormFieldBean;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;

public class MyPersonalDetailsFormBean extends Base {

    private ErrorsBean errors;
    private TitleFormFieldBean salutations;
    private String firstName;
    private String lastName;
    private String email;

    public MyPersonalDetailsFormBean() {
    }

    public MyPersonalDetailsFormBean(@Nullable Customer customer, final UserContext userContext,
                                     final I18nResolver i18nResolver, final Configuration configuration) {
        if (customer != null) {
            this.salutations = new TitleFormFieldBean(customer.getTitle(), userContext, i18nResolver, configuration);
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.email = customer.getEmail();
        }
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

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
