package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(MyOrderListFinderByCustomer.class)
@FunctionalInterface
public interface MyOrderListFinder extends ResourceFinder, Function<Customer, CompletionStage<PagedQueryResult<Order>>> {

    @Override
    CompletionStage<PagedQueryResult<Order>> apply(final Customer customer);
}
