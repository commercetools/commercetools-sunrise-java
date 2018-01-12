package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.changepassword.ChangePasswordReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseChangePasswordController extends SunriseContentController implements WithContent {

    private final ChangePasswordFormAction controllerAction;

    protected SunriseChangePasswordController(final TemplateEngine templateEngine,
                                              final ChangePasswordFormAction controllerAction) {
        super(templateEngine);
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PAGE)
    public CompletionStage<Result> show() {
        return render(OK, PageData.of());
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PROCESS)
    public CompletionStage<Result> process() {
        return controllerAction.apply(this::onPasswordChanged,
                form -> render(BAD_REQUEST, PageData.of().put("changePasswordForm", form)));
    }

    protected abstract Result onPasswordChanged();
}
