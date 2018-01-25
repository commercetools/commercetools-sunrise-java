package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.ResourceUpdater;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

@ImplementedBy(DefaultMyCustomerUpdater.class)
@FunctionalInterface
public interface MyCustomerUpdater extends ResourceUpdater<Customer, CustomerUpdateCommand> {

}
