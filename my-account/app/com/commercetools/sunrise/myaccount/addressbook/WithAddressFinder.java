package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithAddressFinder {

    AddressFinder getAddressFinder();

    default CompletionStage<Result> requireAddress(final Customer customer, final String addressId, final Function<Address, CompletionStage<Result>> nextAction) {
        return getAddressFinder().apply(customer, addressId)
                .thenComposeAsync(addressOpt -> addressOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundAddress),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundAddress();
}
