package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.email.EmailDeliveryException;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This abstract controller gets an customer email as input, request a customer reset password token for the
 * customer and then sends an email with a link to the reset password page.
 */
@IntroducingMultiControllerComponents(SunriseRecoverPasswordHeroldComponent.class)
public abstract class SunriseRecoverPasswordController extends SunriseFrameworkController
        implements WithTemplateName, WithFormFlow<RecoverPasswordFormData, Void, CustomerToken> {

    private static final Logger logger = LoggerFactory.getLogger(SunriseRecoverPasswordController.class);

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "recover-password", "authentication"));
    }

    @Override
    public String getTemplateName() {
        return "my-account-forgot-password";
    }

    @Override
    public Class<? extends RecoverPasswordFormData> getFormDataClass() {
        return DefaultRecoverPasswordFormData.class;
    }

    @SunriseRoute("showRecoveryPasswordForm")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            logger.debug("show recover form in locale={}", languageTag);
            return showForm(null);
        });
    }

    @SunriseRoute("processRecoveryPasswordForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            logger.debug("process recover form in locale={}", languageTag);
            return validateForm(null);
        });
    }

    @Override
    public CompletionStage<? extends CustomerToken> doAction(final RecoverPasswordFormData formData, final Void context) {
        return sphere().execute(CustomerCreatePasswordTokenCommand.of(formData.email()))
                .thenComposeAsync(customerToken -> onResetPasswordTokenCreated(customerToken, formData), HttpExecution.defaultContext());
    }

    protected CompletionStage<CustomerToken> onResetPasswordTokenCreated(final CustomerToken resetPasswordToken, final RecoverPasswordFormData formData) {
        final EmailSender emailSender = injector().getInstance(EmailSender.class);
        return injector().getInstance(RecoverPasswordMessageEditorProvider.class).get(resetPasswordToken, formData)
                .thenCompose(emailSender::send)
                .thenApply(messageId -> resetPasswordToken);
    }

    @Override
    public CompletionStage<Result> handleFailedAction(final Form<? extends RecoverPasswordFormData> form, final Void context, final Throwable throwable) {
        final Throwable cause = throwable.getCause();
        if (cause instanceof EmailDeliveryException) {
            final EmailDeliveryException emailDeliveryException = (EmailDeliveryException) cause;
            return handleEmailDeliveryException(form, emailDeliveryException);
        }
        return WithFormFlow.super.handleFailedAction(form, context, throwable);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends RecoverPasswordFormData> form, final Void context, final ClientErrorException clientErrorException) {
        if (clientErrorException instanceof NotFoundException) {
            return handleNotFoundEmail(form);
        }
        return asyncBadRequest(renderPage(form, context, null));
    }

    protected CompletionStage<Result> handleNotFoundEmail(final Form<? extends RecoverPasswordFormData> form) {
        saveFormError(form, "messages:myAccount.recoverPassword.emailNotFound");
        return asyncBadRequest(renderPage(form, null, null));
    }

    protected CompletionStage<Result> handleEmailDeliveryException(final Form<? extends RecoverPasswordFormData> form, final EmailDeliveryException emailDeliveryException) {
        saveFormError(form, "messages:myAccount.recoverPassword.emailNotDelivered");
        return asyncInternalServerError(renderPage(form, null, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final RecoverPasswordFormData formData, final Void context, final CustomerToken customerToken) {
        flash("success", "messages:myAccount.recoverPassword.emailSent");
        return redirectToSameForm();
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends RecoverPasswordFormData> form, final Void context, final CustomerToken customerToken) {
        final RecoverPasswordPageContent pageContent = injector().getInstance(RecoverPasswordPageContentFactory.class).create(form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void fillFormData(final RecoverPasswordFormData formData, final Void context) {
        // Do nothing
    }

    protected final CompletionStage<Result> redirectToSameForm() {
        final Call call = injector().getInstance(RecoverPasswordReverseRouter.class).showRequestRecoveryEmailForm(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}
