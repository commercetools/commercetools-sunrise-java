package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.SunriseRecoverPasswordController;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This controller performs the reset of a customer's password.
 *
 * It shows a form to enter and confirm the new password {@link #show(String, String)}.
 *
 * It processes the form and sends a password reset command to the commercetools platform
 * {@link #process(String, String)}.
 */
public abstract class SunriseResetPasswordController extends SunriseFrameworkController
        implements WithTemplateName, WithFormFlow<ResetPasswordFormData, String, Customer> {

    private static final Logger logger = LoggerFactory.getLogger(SunriseRecoverPasswordController.class);

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "reset-password", "authentication"));
    }

    @Override
    public String getTemplateName() {
        return "my-account-reset-password";
    }

    @Override
    public Class<? extends ResetPasswordFormData> getFormDataClass() {
        return DefaultResetPasswordFormData.class;
    }

    @SunriseRoute("showResetPasswordForm")
    public CompletionStage<Result> show(final String languageTag, final String resetToken) {
        return doRequest(() -> {
            logger.debug("show reset password form in locale={}", languageTag);
            return showForm(resetToken);
        });
    }

    @SunriseRoute("processResetPasswordForm")
    public CompletionStage<Result> process(final String languageTag, final String resetToken) {
        return doRequest(() -> {
            logger.debug("process reset password form in locale={}", languageTag);
            return validateForm(resetToken);
        });
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends ResetPasswordFormData> form, final String resetToken, final Customer customer) {
        final ResetPasswordPageContent pageContent = injector().getInstance(ResetPasswordPageContentFactory.class).create(resetToken, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public CompletionStage<Customer> doAction(final ResetPasswordFormData formData, final String resetToken) {
        return sphere().execute(CustomerPasswordResetCommand.ofTokenAndPassword(resetToken, formData.newPassword()));
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends ResetPasswordFormData> form, final String resetToken, final ClientErrorException clientErrorException) {
        if (clientErrorException instanceof NotFoundException) {
            return handleNotFoundToken(form, resetToken);
        }
        return asyncBadRequest(renderPage(form, resetToken, null));
    }

    protected CompletionStage<Result> handleNotFoundToken(final Form<? extends ResetPasswordFormData> form, final String resetToken) {
        saveFormError(form, "Reset token is not valid");
        return asyncBadRequest(renderPage(form, resetToken, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ResetPasswordFormData formData, final String context, final Customer result) {
        return redirectToLogInPage();
    }

    @Override
    public void fillFormData(final ResetPasswordFormData formData, final String resetToken) {
        // Do nothing
    }

    protected final CompletionStage<Result> redirectToLogInPage() {
        final Call call = injector().getInstance(AuthenticationReverseRouter.class).showLogInForm(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}
