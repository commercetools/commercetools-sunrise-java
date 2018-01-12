package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.controllers.AbstractControllerAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CustomerSignedInActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerSignInCommandHook;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
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

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.*;
import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;

public class DefaultLogInControllerAction extends AbstractControllerAction<LogInFormData> implements LogInControllerAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogInControllerAction.class);

    private final LogInFormData logInFormData;
    private final MyCartInSession myCartInSession;
    private final SphereClient sphereClient;
    private final HookRunner hookRunner;

    @Inject
    protected DefaultLogInControllerAction(final FormFactory formFactory, final TemplateEngine templateEngine,
                                           final LogInFormData logInFormData, final MyCartInSession myCartInSession,
                                           final SphereClient sphereClient, final HookRunner hookRunner) {
        super(formFactory, templateEngine);
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
    protected String formName() {
        return "logInForm";
    }

    @Override
    protected CompletableFuture<CustomerSignInResult> executeRequest(final LogInFormData formData) {
        return recoverIfMergingCartFailed(formData, executeRequestWithHooks(buildRequest(formData)));
    }

    protected CustomerSignInCommand buildRequest(final LogInFormData formData) {
        return buildBaseRequest(formData)
                .withAnonymousId(myCartInSession.findId().orElse(null));
    }

    @Override
    protected CompletionStage<Result> handleFailedRequest(final Form<? extends LogInFormData> form, final Throwable throwable) {
        if (isCustomerInvalidCredentialsError(throwable.getCause())) {
            form.reject("errors.invalidCredentials");
        }
        return super.handleFailedRequest(form, throwable);
    }

    protected final CompletionStage<CustomerSignInResult> executeRequestWithHooks(final CustomerSignInCommand baseRequest) {
        final CustomerSignInCommand request = CustomerSignInCommandHook.runHook(hookRunner, baseRequest);
        return sphereClient.execute(request)
                .thenComposeAsync(result -> CustomerSignedInActionHook.runHook(hookRunner, result, null)
                                .thenApplyAsync(updatedResult -> {
                                    CustomerSignInResultLoadedHook.runHook(hookRunner, updatedResult);
                                    return updatedResult;
                                }, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }

    private CompletableFuture<CustomerSignInResult> recoverIfMergingCartFailed(final LogInFormData formData,
                                                                               final CompletionStage<CustomerSignInResult> resultStage) {
        return recoverWith(resultStage, throwable -> {
            if (isInvalidInputError(throwable.getCause())) {
                LOGGER.warn("Sign in failed probably due to non-existent cart with given ID, trying to sign in without a cart", throwable);
                return signInWithoutCart(formData);
            } else if (isInvalidOperationError(throwable.getCause())) {
                LOGGER.warn("Sign in failed probably due to merging cart issues, trying to sign in without a cart", throwable);
                return signInWithoutCart(formData);
            }
            return exceptionallyCompletedFuture(throwable);
        }, HttpExecution.defaultContext());
    }

    private CompletionStage<CustomerSignInResult> signInWithoutCart(final LogInFormData formData) {
        return executeRequestWithHooks(buildBaseRequest(formData));
    }

    private CustomerSignInCommand buildBaseRequest(final LogInFormData formData) {
        return CustomerSignInCommand.of(formData.username(), formData.password());
    }
}
