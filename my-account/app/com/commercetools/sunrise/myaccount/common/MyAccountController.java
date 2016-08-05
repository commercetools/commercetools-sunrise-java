package com.commercetools.sunrise.myaccount.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.hooks.events.CustomerLoadedHook;
import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public abstract class MyAccountController extends SunriseFrameworkController {

    @Inject
    private Injector injector;

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }

    protected CompletionStage<Optional<Customer>> getCustomer(final Http.Session session) {
        return injector.getInstance(CustomerFinderBySession.class).findCustomer(session);
    }

    protected CompletionStage<Result> ifValidCustomer(@Nullable final Customer customer,
                                                      final Function<Customer, CompletionStage<Result>> onValidCustomer) {
        return Optional.ofNullable(customer)
                .map(notNullCustomer -> CustomerLoadedHook.runHook(hooks(), customer)
                        .thenComposeAsync(unused -> onValidCustomer.apply(notNullCustomer), HttpExecution.defaultContext()))
                .orElseGet(this::handleNotFoundCustomer);
    }

    protected CompletionStage<Result> handleNotFoundCustomer() {
        final UserContext userContext = injector.getInstance(UserContext.class);
        final ReverseRouter reverseRouter = injector.getInstance(ReverseRouter.class);
        final Call call = reverseRouter.showLogInForm(userContext.languageTag());
        return completedFuture(redirect(call));
    }
}
