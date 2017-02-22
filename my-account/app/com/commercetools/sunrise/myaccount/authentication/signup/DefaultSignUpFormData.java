package com.commercetools.sunrise.myaccount.authentication.signup;

import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultSignUpFormData extends Base implements SignUpFormData {

    private String title;
    @Required
    private String firstName;
    @Required
    private String lastName;
    @Required
    private String email;
    @Required
    private String password;
    @Required
    private String confirmPassword;
    @Required
    private boolean agreeToTerms;

    public String validate() {
        if (!password.equals(confirmPassword)) {
            return "Not matching passwords"; // TODO use i18n version
        }
        if (!agreeToTerms) {
            return "You must agree to terms"; // TODO use i18n version
        }
        return null;
    }

    @Override
    public CustomerDraft customerDraft() {
        return CustomerDraftBuilder.of(email, password)
                .title(title)
                .firstName(firstName)
                .lastName(lastName)
                .build();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(final boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }
}

