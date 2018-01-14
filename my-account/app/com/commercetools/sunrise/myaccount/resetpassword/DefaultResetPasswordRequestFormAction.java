package com.commercetools.sunrise.myaccount.resetpassword;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerTokenCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerCreatePasswordTokenCommandHook;
import com.commercetools.sunrise.email.EmailDeliveryException;
import com.commercetools.sunrise.email.EmailSender;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultResetPasswordRequestFormAction extends AbstractFormAction<ResetPasswordRequestFormData> implements ResetPasswordRequestFormAction {

    private final ResetPasswordRequestFormData formData;
    private final SphereClient sphereClient;
    private final HookRunner hookRunner;
    private final EmailSender emailSender;
    private final ResetPasswordRequestMessageEditorProvider messageEditorProvider;

    @Inject
    protected DefaultResetPasswordRequestFormAction(final FormFactory formFactory, final ResetPasswordRequestFormData formData,
                                                    final SphereClient sphereClient, final HookRunner hookRunner,
                                                    final EmailSender emailSender,
                                                    final ResetPasswordRequestMessageEditorProvider messageEditorProvider) {
        super(formFactory);
        this.formData = formData;
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
        this.emailSender = emailSender;
        this.messageEditorProvider = messageEditorProvider;
    }

    @Override
    protected Class<? extends ResetPasswordRequestFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ResetPasswordRequestFormData formData) {
        return executeRequestWithHooks(CustomerCreatePasswordTokenCommand.of(formData.email()))
                .thenComposeAsync(token -> messageEditorProvider.get(token, formData)
                        .thenComposeAsync(emailSender::send, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }

    @Override
    protected CompletionStage<Result> onFailedRequest(final Form<? extends ResetPasswordRequestFormData> form, final Throwable throwable,
                                                      final Function<Form<? extends ResetPasswordRequestFormData>, CompletionStage<Result>> onBadRequest) {
        if (throwable.getCause() instanceof NotFoundException) {
            form.reject("errors.emailNotFound");
        } else if (throwable.getCause() instanceof EmailDeliveryException) {
            form.reject("errors.emailDelivery");
        } else {
            return super.onFailedRequest(form, throwable, onBadRequest);
        }
        return onBadRequest.apply(form);
    }

    protected final CompletionStage<CustomerToken> executeRequestWithHooks(final CustomerCreatePasswordTokenCommand baseCommand) {
        final CompletionStage<CustomerToken> resultStage = CustomerCreatePasswordTokenCommandHook.runHook(hookRunner, baseCommand)
                .thenCompose(sphereClient::execute);
        resultStage.thenAcceptAsync(result -> CustomerTokenCreatedHook.runHook(hookRunner, result), HttpExecution.defaultContext());
        return resultStage;
    }
}
