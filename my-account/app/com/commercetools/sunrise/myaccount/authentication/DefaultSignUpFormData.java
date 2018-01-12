package com.commercetools.sunrise.myaccount.authentication;

import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultSignUpFormData extends Base implements SignUpFormData {

    private String title;

    @Constraints.Required
    private String firstName;

    @Constraints.Required
    private String lastName;

    @Constraints.Required
    private String email;

    @Constraints.Required
    @Constraints.MinLength(4)
    private String password;


    @Constraints.Required
    private String confirmPassword;

    @Constraints.Required
    private boolean agreeToTerms;

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

