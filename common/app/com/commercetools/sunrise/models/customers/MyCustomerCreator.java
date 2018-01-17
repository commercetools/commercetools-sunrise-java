package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCustomerCreator.class)
public interface MyCustomerCreator extends ResourceCreator<CustomerSignInResult, CustomerDraft> {

    CompletionStage<CustomerDraft> defaultDraft(String email, String password);

    default CompletionStage<CustomerSignInResult> get(String email, String password) {
        return defaultDraft(email, password).thenComposeAsync(this::get, HttpExecution.defaultContext());
    }
}
