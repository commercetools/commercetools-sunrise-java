package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.controllers.WithForm;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.changepassword.ChangePasswordReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isCustomerInvalidCurrentPasswordError;

public abstract class SunriseChangePasswordController extends SunriseContentController implements WithContent, WithForm<ChangePasswordFormData> {

    private final ChangePasswordControllerAction controllerAction;

    protected SunriseChangePasswordController(final ContentRenderer contentRenderer,
                                              final ChangePasswordControllerAction controllerAction) {
        super(contentRenderer);
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PAGE)
    public CompletionStage<Result> show() {
        return okResult(PageData.of());
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PROCESS)
    public CompletionStage<Result> process() {
        final Form<? extends ChangePasswordFormData> form = controllerAction.bindForm();
        if (form.hasErrors()) {
            return badRequestResult(PageData.of().putField("changePasswordForm", form));
        } else {
            return controllerAction.apply(form.get())
                    .thenApplyAsync(x -> handleSuccessfulAction(), HttpExecution.defaultContext());
        }
    }

    @Override
    public CompletionStage<Result> handleFailedAction(final Form<? extends ChangePasswordFormData> form, final Throwable throwable) {
        if (isCustomerInvalidCurrentPasswordError(throwable.getCause())) {
            form.reject("errors.invalidCurrentPassword");
            return badRequestResult(PageData.of().putField("changePasswordForm", form));
        } else {
            return WithForm.super.handleFailedAction(form, throwable);
        }
    }
}
