package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * This controller performs the reset of a customer's password.
 *
 * It shows a form to enter and confirm the new password {@link #show(String)}.
 *
 * It processes the form and sends a password reset command to the commercetools platform
 * {@link #process(String)}.
 */
public abstract class SunriseResetPasswordController extends SunriseContentFormController implements MyAccountController, WithContentForm2Flow<String, Customer, ResetPasswordFormData> {

    private final ResetPasswordFormData formData;
    private final ResetPasswordControllerAction controllerAction;

    public SunriseResetPasswordController(final ContentRenderer contentRenderer,
                                          final FormFactory formFactory,
                                          final ResetPasswordFormData formData,
                                          final ResetPasswordControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(RecoverPasswordReverseRouter.RESET_PASSWORD_PAGE)
    public CompletionStage<Result> show(final String resetToken) {
        return showFormPage(resetToken, formData);
    }

    @EnableHooks
    @SunriseRoute(RecoverPasswordReverseRouter.RESET_PASSWORD_PROCESS)
    public CompletionStage<Result> process(final String resetToken) {
        return processForm(resetToken);
    }

    @Override
    public Class<? extends ResetPasswordFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public PageContent createPageContent(final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public CompletionStage<Customer> executeAction(final String resetToken, final ResetPasswordFormData formData) {
        return controllerAction.apply(resetToken, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final String resetToken, final Form<? extends ResetPasswordFormData> form, final ClientErrorException clientErrorException) {
        if (clientErrorException instanceof NotFoundException) {
            return handleNotFoundToken(resetToken, form);
        }
        return WithContentForm2Flow.super.handleClientErrorFailedAction(resetToken, form, clientErrorException);
    }

    protected abstract CompletionStage<Result> handleNotFoundToken(final String resetToken, final Form<? extends ResetPasswordFormData> form);

    @Override
    public void preFillFormData(final String resetToken, final ResetPasswordFormData formData) {
        // Do nothing
    }
}
