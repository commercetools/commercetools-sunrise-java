package com.commercetools.sunrise.myaccount.authentication.logout;

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

@IntroducingMultiControllerComponents(LogOutThemeLinksControllerComponent.class)
public abstract class SunriseLogOutController extends SunriseFrameworkController {

    private final CustomerInSession customerInSession;
    private final CartInSession cartInSession;

    protected SunriseLogOutController(final CustomerInSession customerInSession, final CartInSession cartInSession) {
        this.customerInSession = customerInSession;
        this.cartInSession = cartInSession;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "log-out", "customer", "user"));
    }

    @SunriseRoute("processLogOut")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            doAction();
            return handleSuccessfulAction();
        });
    }

    protected void doAction() {
        customerInSession.remove();
        cartInSession.remove();
    }

    protected abstract CompletionStage<Result> handleSuccessfulAction();
}
