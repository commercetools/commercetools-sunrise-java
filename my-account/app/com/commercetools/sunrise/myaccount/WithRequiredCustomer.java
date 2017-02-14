package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredCustomer {

    CustomerFinder getCustomerFinder();

    default CompletionStage<Result> requireCustomer(final Function<Customer, CompletionStage<Result>> nextAction) {
        return getCustomerFinder().get()
                .thenComposeAsync(customer -> customer
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCustomer),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundCustomer();
}
