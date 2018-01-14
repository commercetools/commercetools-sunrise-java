package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAuthenticationController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final SignUpFormAction signUpFormAction;
    private final LogInFormAction logInFormAction;
    private final LogOutAction logOutAction;

    protected SunriseAuthenticationController(final TemplateEngine templateEngine,
                                              final SignUpFormAction signUpFormAction,
                                              final LogInFormAction logInFormAction,
                                              final LogOutAction logOutAction) {
        this.templateEngine = templateEngine;
        this.signUpFormAction = signUpFormAction;
        this.logInFormAction = logInFormAction;
        this.logOutAction = logOutAction;
    }

    protected final TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render("my-account-login")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> signUp() {
        return signUpFormAction.apply(this::onSignedUp,
                form -> {
                    final PageData pageData = PageData.of().put("signUpForm", form);
                    return templateEngine.render("my-account-login", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> logIn() {
        return logInFormAction.apply(this::onLoggedIn,
                form -> {
                    final PageData pageData = PageData.of().put("logInForm", form);
                    return templateEngine.render("my-account-login", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> logOut() {
        return logOutAction.apply(this::onLoggedOut);
    }

    public abstract Result onSignedUp();

    public abstract Result onLoggedIn();

    public abstract Result onLoggedOut();
}
