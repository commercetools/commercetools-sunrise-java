package com.commercetools.sunrise.myaccount.mydetails;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyPersonalDetailsUpdater.class)
public interface MyPersonalDetailsUpdater {

    CompletionStage<Customer> updateCustomer(final Customer customer, final MyPersonalDetailsFormData formData);
}
