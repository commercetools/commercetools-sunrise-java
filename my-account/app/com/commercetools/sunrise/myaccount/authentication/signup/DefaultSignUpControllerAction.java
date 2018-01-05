package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.models.customers.CustomerCreator;
import com.commercetools.sunrise.models.customers.SignUpFormData;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultSignUpControllerAction implements SignUpControllerAction {

    private final CustomerCreator customerCreator;

    @Inject
    DefaultSignUpControllerAction(final CustomerCreator customerCreator) {
        this.customerCreator = customerCreator;
    }

    @Override
    public CompletionStage<CustomerSignInResult> apply(final SignUpFormData formData) {
        return customerCreator.apply(formData);
    }
}
