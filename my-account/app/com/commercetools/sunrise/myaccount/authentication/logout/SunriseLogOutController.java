package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import com.commercetools.sunrise.shoppingcart.CartInSession;
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
        doAction();
        return handleSuccessfulAction();
    }

    protected void doAction() {
        injector().getInstance(CustomerInSession.class).remove();
        injector().getInstance(CartInSession.class).remove();
    }

    protected CompletionStage<Result> handleSuccessfulAction() {
        return redirectToHome();
    }
}
