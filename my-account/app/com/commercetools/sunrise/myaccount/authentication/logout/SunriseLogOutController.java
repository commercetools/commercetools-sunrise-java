package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerSessionHandler;
import com.commercetools.sunrise.shoppingcart.CartSessionHandler;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@RequestScoped
@IntroducingMultiControllerComponents(SunriseLogOutHeroldComponent.class)
public abstract class SunriseLogOutController extends SunriseFrameworkController {

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "log-out", "customer", "user"));
    }

    @SunriseRoute("processLogOut")
    public CompletionStage<Result> process(final String languageTag) {
        final Http.Session session = session();
        doAction(session);
        return handleSuccessfulAction();
    }

    protected void doAction(final Http.Session session) {
        injector().getInstance(CustomerSessionHandler.class).removeFromSession(session);
        injector().getInstance(CartSessionHandler.class).removeFromSession(session);
    }

    protected CompletionStage<Result> handleSuccessfulAction() {
        return redirectToHome();
    }
}
