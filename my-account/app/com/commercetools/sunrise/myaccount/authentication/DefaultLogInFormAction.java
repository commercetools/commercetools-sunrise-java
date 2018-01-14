package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerSignInCommandHook;
import com.commercetools.sunrise.models.carts.MyCartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.*;
import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;

public class DefaultLogInFormAction extends AbstractFormAction<LogInFormData> implements LogInFormAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogInFormAction.class);

    private final LogInFormData logInFormData;
    private final MyCartInSession myCartInSession;
    private final SphereClient sphereClient;
    private final HookRunner hookRunner;

    @Inject
    protected DefaultLogInFormAction(final FormFactory formFactory, final LogInFormData logInFormData,
                                     final MyCartInSession myCartInSession,
                                     final SphereClient sphereClient, final HookRunner hookRunner) {
        super(formFactory);
        this.logInFormData = logInFormData;
        this.myCartInSession = myCartInSession;
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
    }

    @Override
    protected Class<? extends LogInFormData> formClass() {
        return logInFormData.getClass();
    }

    @Override
    protected CompletableFuture<CustomerSignInResult> onValidForm(final LogInFormData formData) {
        return recoverIfMergingCartFailed(formData, signInWithAnonymousCart(formData));
    }

    @Override
    protected CompletionStage<Result> onFailedRequest(final Form<? extends LogInFormData> form, final Throwable throwable,
                                                      final Function<Form<? extends LogInFormData>, CompletionStage<Result>> onBadRequest) {
        if (isCustomerInvalidCredentialsError(throwable.getCause())) {
            form.reject("errors.invalidCredentials");
            return onBadRequest.apply(form);
        }
        return super.onFailedRequest(form, throwable, onBadRequest);
    }

    protected final CompletionStage<CustomerSignInResult> executeWithHooks(final CustomerSignInCommand baseRequest) {
        return CustomerSignInCommandHook.runHook(hookRunner, baseRequest)
                .thenCompose(sphereClient::execute)
                .thenComposeAsync(this::applyHooks, HttpExecution.defaultContext());
    }

    private CompletionStage<CustomerSignInResult> applyHooks(final CustomerSignInResult result) {
        final CompletionStage<CustomerSignInResult> finalResultStage = CustomerSignedInActionHook.runHook(hookRunner, result, null);
        finalResultStage.thenAcceptAsync(finalResult -> CustomerSignInResultLoadedHook.runHook(hookRunner, finalResult), HttpExecution.defaultContext());
        return finalResultStage;
    }

    private CompletableFuture<CustomerSignInResult> recoverIfMergingCartFailed(final LogInFormData formData,
                                                                               final CompletionStage<CustomerSignInResult> resultStage) {
        return recoverWith(resultStage, throwable -> {
            if (isInvalidInputError(throwable.getCause())) {
                LOGGER.warn("Sign in failed probably due to non-existent cart with given ID, trying to sign in without a cart", throwable);
                return signInWithoutAnonymousCart(formData);
            } else if (isInvalidOperationError(throwable.getCause())) {
                LOGGER.warn("Sign in failed probably due to merging cart issues, trying to sign in without a cart", throwable);
                return signInWithoutAnonymousCart(formData);
            }
            return exceptionallyCompletedFuture(throwable);
        }, HttpExecution.defaultContext());
    }

    private CompletionStage<CustomerSignInResult> signInWithAnonymousCart(final LogInFormData formData) {
        return executeWithHooks(buildRequest(formData));
    }

    private CompletionStage<CustomerSignInResult> signInWithoutAnonymousCart(final LogInFormData formData) {
        return executeWithHooks(buildRequest(formData).withAnonymousId(null));
    }

    private CustomerSignInCommand buildRequest(final LogInFormData formData) {
        return CustomerSignInCommand.of(formData.username(), formData.password())
                .withAnonymousId(myCartInSession.findId().orElse(null));
    }
}
