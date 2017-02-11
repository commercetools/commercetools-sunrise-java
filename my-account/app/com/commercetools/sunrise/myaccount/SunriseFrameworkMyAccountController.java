package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import io.sphere.sdk.customers.Customer;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@NoCache
public abstract class SunriseFrameworkMyAccountController extends SunriseFrameworkController {

    private final CustomerFinder customerFinder;

    protected SunriseFrameworkMyAccountController(final CustomerFinder customerFinder) {
        this.customerFinder = customerFinder;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }

    protected final CompletionStage<Result> requireCustomer(final Function<Customer, CompletionStage<Result>> nextAction) {
        return customerFinder.findCustomer()
                .thenComposeAsync(customer -> customer
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCustomer),
                        HttpExecution.defaultContext());
    }

    protected abstract CompletionStage<Result> handleNotFoundCustomer();
}
