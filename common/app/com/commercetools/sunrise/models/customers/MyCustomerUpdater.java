package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCustomerUpdaterImpl.class)
@FunctionalInterface
public interface MyCustomerUpdater extends ResourceUpdater<Customer> {

    @Override
    CompletionStage<Optional<Customer>> apply(List<? extends UpdateAction<Customer>> updateActions);
}
