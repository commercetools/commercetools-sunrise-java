package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.core.controllers.SunriseController;
import com.commercetools.sunrise.core.controllers.WithExecutionFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.client.ClientErrorException;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseLogOutController extends SunriseController
        implements MyAccountController, WithExecutionFlow<Void, Void> {

    private final LogOutControllerAction logOutControllerAction;

    protected SunriseLogOutController(final LogOutControllerAction logOutControllerAction) {
        this.logOutControllerAction = logOutControllerAction;
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_OUT_PROCESS)
    public CompletionStage<Result> process() {
        return processRequest(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input) {
        logOutControllerAction.run();
        return completedFuture(null);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final ClientErrorException clientErrorException) {
        // No CTP call involved, this should never be called
        return handleGeneralFailedAction(clientErrorException);
    }
}
