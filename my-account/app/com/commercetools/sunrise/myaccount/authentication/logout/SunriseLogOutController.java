package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.controllers.SunriseController;
import com.commercetools.sunrise.common.controllers.WithExecutionFlow;
import com.commercetools.sunrise.common.sessions.cart.CartInSession;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import io.sphere.sdk.client.ClientErrorException;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseLogOutController extends SunriseController implements WithExecutionFlow<Void, Void> {

    private final CustomerInSession customerInSession;
    private final CartInSession cartInSession;

    protected SunriseLogOutController(final CustomerInSession customerInSession, final CartInSession cartInSession) {
        this.customerInSession = customerInSession;
        this.cartInSession = cartInSession;
    }

    @RunRequestStartedHook
    @SunriseRoute("processLogOut")
    public CompletionStage<Result> process(final String languageTag) {
        return processRequest(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input) {
        customerInSession.remove();
        cartInSession.remove();
        return completedFuture(null);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final ClientErrorException clientErrorException) {
        // No CTP call involved, this should never be called
        return handleGeneralFailedAction(clientErrorException);
    }
}
