package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.myaccount.CustomerSessionHandler;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.shoppingcart.CartSessionUtils.removeCartSessionData;
import static java.util.Arrays.asList;

@RequestScoped
public abstract class SunriseLogOutController extends SunriseFrameworkController {

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "log-out", "customer", "user"));
    }

    public CompletionStage<Result> process(final String languageTag) {
        injector().getInstance(CustomerSessionHandler.class).removeFromSession(session());
        removeCartSessionData(session());
        return redirectToHome();
    }
}
