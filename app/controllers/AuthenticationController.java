package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.myaccount.authentication.LogInFormAction;
import com.commercetools.sunrise.myaccount.authentication.LogOutAction;
import com.commercetools.sunrise.myaccount.authentication.SignUpFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class AuthenticationController extends SunriseController {

    private static final String TEMPLATE = "my-account-login";

    private final TemplateEngine templateEngine;
    private final SignUpFormAction signUpFormAction;
    private final LogInFormAction logInFormAction;
    private final LogOutAction logOutAction;

    @Inject
    AuthenticationController(final TemplateEngine templateEngine,
                             final SignUpFormAction signUpFormAction,
                             final LogInFormAction logInFormAction,
                             final LogOutAction logOutAction) {
        this.templateEngine = templateEngine;
        this.signUpFormAction = signUpFormAction;
        this.logInFormAction = logInFormAction;
        this.logOutAction = logOutAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> signUp() {
        return signUpFormAction.apply(
                () -> routes.CustomerProfileController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("signUpForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> logIn() {
        return logInFormAction.apply(
                () -> routes.CustomerProfileController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("logInForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> logOut() {
        return logOutAction.apply(() -> routes.AuthenticationController.show());
    }
}
