package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.controllers.SunriseController;
import com.commercetools.sunrise.common.sessions.cart.CartInSession;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseLogOutController extends SunriseController {

    private final CustomerInSession customerInSession;
    private final CartInSession cartInSession;

    protected SunriseLogOutController(final ComponentRegistry componentRegistry, final CustomerInSession customerInSession, final CartInSession cartInSession) {
        super(componentRegistry);
        this.customerInSession = customerInSession;
        this.cartInSession = cartInSession;
    }

    @Inject
    private void registerThemeLinks(final LogOutThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
    }

    @SunriseRoute("processLogOut")
    public CompletionStage<Result> process(final String languageTag) {
        return executeAction()
                .thenComposeAsync(unused -> handleSuccessfulAction(), HttpExecution.defaultContext());
    }

    protected CompletionStage<Void> executeAction() {
        customerInSession.remove();
        cartInSession.remove();
        return completedFuture(null);
    }

    protected abstract CompletionStage<Result> handleSuccessfulAction();
}
