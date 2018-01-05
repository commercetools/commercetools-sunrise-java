package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultCustomerCreator.class)
@FunctionalInterface
public interface CustomerCreator extends ResourceCreator<CustomerSignInResult>, Function<SignUpFormData, CompletionStage<CustomerSignInResult>> {

    @Override
    CompletionStage<CustomerSignInResult> apply(SignUpFormData formData);
}
