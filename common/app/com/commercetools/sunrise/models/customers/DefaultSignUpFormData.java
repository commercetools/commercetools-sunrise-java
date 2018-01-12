package com.commercetools.sunrise.models.customers;

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
            return "errors.notMatchingPasswords";
        }
        if (!agreeToTerms) {
            return "errors.agreeToTerms";
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
}

