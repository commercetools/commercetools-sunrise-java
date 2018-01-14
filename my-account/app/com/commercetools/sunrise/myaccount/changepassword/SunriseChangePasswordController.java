package com.commercetools.sunrise.myaccount.changepassword;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseChangePasswordController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final ChangePasswordFormAction formAction;

    protected SunriseChangePasswordController(final TemplateEngine templateEngine,
                                              final ChangePasswordFormAction formAction) {
        this.templateEngine = templateEngine;
        this.formAction = formAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render("my-account-change-password")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> change() {
        return formAction.apply(this::onChanged,
                form -> {
                    final PageData pageData = PageData.of().put("changePasswordForm", form);
                    return templateEngine.render("my-account-change-password", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onChanged();
}
