package com.commercetools.sunrise.myaccount.authentication;

import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultSignUpFormData extends Base implements SignUpFormData {

    public String title;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String email;

    @Constraints.Required
    @Constraints.MinLength(4)
    public String password;


    @Constraints.Required
    public String confirmPassword;

    @Constraints.Required
    public boolean agreeToTerms;

    @Override
    public CustomerDraft customerDraft() {
        return CustomerDraftBuilder.of(email, password)
                .title(title)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public String validate() {
        if (!password.equals(confirmPassword)) {
            return "errors.notMatchingPasswords";
        }
        if (!agreeToTerms) {
            return "errors.agreeToTerms";
        }
        return null;
    }
}

