package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.myaccount.authentication.LogInFormAction;
import com.commercetools.sunrise.myaccount.authentication.LogOutAction;
import com.commercetools.sunrise.myaccount.authentication.SignUpFormAction;
import com.commercetools.sunrise.myaccount.authentication.SunriseAuthenticationController;
import controllers.myaccount.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class AuthenticationController extends SunriseAuthenticationController {

    @Inject
    AuthenticationController(final TemplateEngine templateEngine,
                             final SignUpFormAction signUpFormAction,
                             final LogInFormAction logInFormAction,
                             final LogOutAction logOutAction) {
        super(templateEngine, signUpFormAction, logInFormAction, logOutAction);
    }

    @Override
    public Result onSignedUp() {
        return redirect(controllers.myaccount.routes.CustomerProfileController.show());
    }

    @Override
    public Result onLoggedIn() {
        return redirect(controllers.myaccount.routes.CustomerProfileController.show());
    }

    @Override
    public Result onLoggedOut() {
        return redirect(routes.AuthenticationController.show());
    }
}
