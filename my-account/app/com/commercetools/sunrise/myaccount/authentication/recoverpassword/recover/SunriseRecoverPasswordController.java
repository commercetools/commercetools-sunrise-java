package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordPageContentFactory;
import io.commercetools.sunrise.email.EmailDeliveryException;
import io.sphere.sdk.customers.CustomerToken;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * This abstract controller gets an customer email as input, request a customer reset password token for the
 * customer and then sends an email with a link to the reset password page.
 */
public abstract class SunriseRecoverPasswordController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<Void, CustomerToken, RecoverPasswordFormData> {
    private final RecoverPasswordPageContentFactory pageContentFactory;
    private final RecoverPasswordControllerAction controllerAction;
    private final RecoverPasswordFormData formData;

    protected SunriseRecoverPasswordController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                               final RecoverPasswordPageContentFactory pageContentFactory,
                                               final RecoverPasswordFormData formData,
                                               final RecoverPasswordControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
        this.formData = formData;
    }

    @Override
    public final Class<? extends RecoverPasswordFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(RecoverPasswordReverseRouter.REQUEST_RECOVERY_EMAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return showFormPage(null, formData);
    }

    @EnableHooks
    @SunriseRoute(RecoverPasswordReverseRouter.REQUEST_RECOVERY_EMAIL_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<CustomerToken> executeAction(final Void input, final RecoverPasswordFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleFailedAction(final Void input, final Form<? extends RecoverPasswordFormData> form, final Throwable throwable) {
        final Throwable cause = throwable.getCause();
        if (cause instanceof EmailDeliveryException) {
            final EmailDeliveryException emailDeliveryException = (EmailDeliveryException) cause;
            return handleEmailDeliveryException(form, emailDeliveryException);
        }
        return WithContentFormFlow.super.handleFailedAction(input, form, throwable);
    }

    protected abstract CompletionStage<Result> handleEmailDeliveryException(final Form<? extends RecoverPasswordFormData> form, final EmailDeliveryException emailDeliveryException);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends RecoverPasswordFormData> form) {
        return pageContentFactory.create(form);
    }

    @Override
    public void preFillFormData(final Void input, final RecoverPasswordFormData formData) {
        // Do nothing
    }
}
