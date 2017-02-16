package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.controllers.SunriseController;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.common.sessions.customers.CustomerInSession;
import com.commercetools.sunrise.common.sessions.carts.CartInSession;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(LogOutThemeLinksControllerComponent.class)
public abstract class SunriseLogOutController extends SunriseController {

    private final CustomerInSession customerInSession;
    private final CartInSession cartInSession;

    protected SunriseLogOutController(final RequestHookContext hookContext, final CustomerInSession customerInSession, final CartInSession cartInSession) {
        super(hookContext);
        this.customerInSession = customerInSession;
        this.cartInSession = cartInSession;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "log-out", "customer", "user"));
    }

    @SunriseRoute("processLogOut")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> executeAction()
                .thenComposeAsync(unused -> handleSuccessfulAction(), HttpExecution.defaultContext()));
    }

    protected CompletionStage<Void> executeAction() {
        customerInSession.remove();
        cartInSession.remove();
        return completedFuture(null);
    }

    protected abstract CompletionStage<Result> handleSuccessfulAction();
}
