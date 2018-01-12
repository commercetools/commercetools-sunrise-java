package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAuthenticationController extends SunriseContentController implements WithContent {

    private final SignUpControllerAction signUpControllerAction;
    private final LogInControllerAction logInControllerAction;
    private final LogOutControllerAction logOutControllerAction;

    protected SunriseAuthenticationController(final TemplateEngine templateEngine,
                                              final SignUpControllerAction signUpControllerAction,
                                              final LogInControllerAction logInControllerAction,
                                              final LogOutControllerAction logOutControllerAction) {
        super(templateEngine);
        this.signUpControllerAction = signUpControllerAction;
        this.logInControllerAction = logInControllerAction;
        this.logOutControllerAction = logOutControllerAction;
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_IN_PAGE)
    public CompletionStage<Result> show() {
        return render(OK, PageData.of());
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.SIGN_UP_PROCESS)
    public CompletionStage<Result> processSignUp() {
        return signUpControllerAction.apply(this::handleSuccessfulSignUp);
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_IN_PROCESS)
    public CompletionStage<Result> processLogIn() {
        return logInControllerAction.apply(this::handleSuccessfulLogIn);
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_OUT_PROCESS)
    public CompletionStage<Result> processLogOut() {
        return logOutControllerAction.apply(this::handleSuccessfulLogOut);
    }

    public abstract Result handleSuccessfulSignUp();

    public abstract Result handleSuccessfulLogIn();

    public abstract Result handleSuccessfulLogOut();
}
