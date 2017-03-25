package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.authentication.AbstractCustomerSignInExecutor;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.ctp.CtpExceptionUtils.isInvalidInputError;
import static com.commercetools.sunrise.ctp.CtpExceptionUtils.isInvalidOperationError;
import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;

public class DefaultLogInControllerAction extends AbstractCustomerSignInExecutor implements LogInControllerAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogInControllerAction.class);
    private final CartInSession cartInSession;

    @Inject
    protected DefaultLogInControllerAction(final SphereClient sphereClient, final HookRunner hookRunner, final CartInSession cartInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
    }

    @Override
    public CompletionStage<CustomerSignInResult> apply(final LogInFormData formData) {
        final CompletionStage<CustomerSignInResult> resultStage = executeRequest(buildRequest(formData));
        return resultOrRecoverIfMergingCartFailed(resultStage, formData);
    }

    protected CustomerSignInCommand buildRequest(final LogInFormData formData) {
        final String cartId = cartInSession.findCartId().orElse(null);
        return CustomerSignInCommand.of(formData.username(), formData.password(), cartId);
    }

    protected final CompletableFuture<CustomerSignInResult> resultOrRecoverIfMergingCartFailed(final CompletionStage<CustomerSignInResult> resultStage,
                                                                                               final LogInFormData formData) {
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
        return executeRequest(CustomerSignInCommand.of(formData.username(), formData.password()));
    }
}
