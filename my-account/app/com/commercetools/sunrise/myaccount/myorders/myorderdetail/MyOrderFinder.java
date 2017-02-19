package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(MyOrderFinderByCustomer.class)
public interface MyOrderFinder extends ResourceFinder, BiFunction<Customer, String, CompletionStage<Optional<Order>>> {

    @Override
    CompletionStage<Optional<Order>> apply(final Customer customer, final String identifier);
}
