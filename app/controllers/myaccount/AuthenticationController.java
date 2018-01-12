package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.myaccount.authentication.LogOutControllerAction;
import com.commercetools.sunrise.myaccount.authentication.LogInControllerAction;
import com.commercetools.sunrise.myaccount.authentication.SunriseAuthenticationController;
import com.commercetools.sunrise.myaccount.authentication.SignUpControllerAction;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class AuthenticationController extends SunriseAuthenticationController {

    @Inject
    AuthenticationController(final TemplateEngine templateEngine,
                             final SignUpControllerAction signUpControllerAction,
                             final LogInControllerAction logInControllerAction,
                             final LogOutControllerAction logOutControllerAction) {
        super(templateEngine, signUpControllerAction, logInControllerAction, logOutControllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    @Override
    public Result handleSuccessfulSignUp() {
        return redirect(routes.MyPersonalDetailsController.show());
    }

    @Override
    public Result handleSuccessfulLogIn() {
        return redirect(routes.MyPersonalDetailsController.show());
    }

    @Override
    public Result handleSuccessfulLogOut() {
        return redirect(routes.AuthenticationController.show());
    }
}
