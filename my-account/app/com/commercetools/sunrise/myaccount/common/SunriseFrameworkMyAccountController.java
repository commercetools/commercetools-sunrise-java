package com.commercetools.sunrise.myaccount.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import org.slf4j.LoggerFactory;
import play.mvc.Call;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@NoCache
public abstract class SunriseFrameworkMyAccountController extends SunriseFrameworkController {

    @Inject
    private Injector injector;

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }

    @Inject
    private void postInit() {
        //just prepend another error handler if this does not suffice
        prependErrorHandler(e -> e instanceof CustomerNotFoundException, e -> {
            LoggerFactory.getLogger(SunriseFrameworkMyAccountController.class).error("access denied", e);
            return handleNotFoundCustomer();
        });
    }

    protected CompletionStage<Optional<Customer>> findCustomer() {
        return injector().getInstance(CustomerFinderBySession.class).findCustomer(null);
    }

    /**
     * Searches for an existing customer in the platform, otherwise the stage contains a {@link CustomerNotFoundException}.
     * @return stage with the customer in session, or {@link CustomerNotFoundException}
     */
    protected CompletionStage<Customer> requireExistingCustomer() {
        return findCustomer()
                .thenApplyAsync(customerOpt -> customerOpt
                        .orElseThrow(CustomerNotFoundException::new), defaultContext());
    }

    protected String requireExistingCustomerId() {
        return injector().getInstance(CustomerInSession.class).findCustomerId()
                .orElseThrow(CustomerNotFoundException::new);
    }

    protected CompletionStage<Result> handleNotFoundCustomer() {
        final UserContext userContext = injector.getInstance(UserContext.class);
        final AuthenticationReverseRouter reverseRouter = injector.getInstance(AuthenticationReverseRouter.class);
        final Call call = reverseRouter.showLogInForm(userContext.languageTag());
        return completedFuture(redirect(call));
    }
}
