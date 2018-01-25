package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCustomerCreator.class)
public interface MyCustomerCreator extends ResourceCreator {

    CompletionStage<CustomerSignInResult> get(CustomerDraft customerDraft);
}
