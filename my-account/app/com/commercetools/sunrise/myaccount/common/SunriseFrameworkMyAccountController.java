package com.commercetools.sunrise.myaccount.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@NoCache
public abstract class SunriseFrameworkMyAccountController extends SunriseFrameworkController {

    private final AuthenticationLocalizedReverseRouter authenticationReverseRouter;

    protected SunriseFrameworkMyAccountController(final AuthenticationLocalizedReverseRouter authenticationReverseRouter) {
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }

    protected abstract CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
