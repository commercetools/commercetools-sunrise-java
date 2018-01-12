package com.commercetools.sunrise.myaccount.authentication.resetpassword;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerPasswordResetCommandHook;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultResetPasswordFormAction extends AbstractFormAction<ResetPasswordFormData> implements ResetPasswordFormAction {

    private final ResetPasswordFormData formData;
    private final SphereClient sphereClient;
    private final HookRunner hookRunner;

    @Inject
    protected DefaultResetPasswordFormAction(final FormFactory formFactory, final ResetPasswordFormData formData,
                                             final SphereClient sphereClient, final HookRunner hookRunner) {
        super(formFactory);
        this.formData = formData;
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
    }

    @Override
    protected Class<? extends ResetPasswordFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ResetPasswordFormData formData) {
        return executeWithHooks(CustomerPasswordResetCommand.ofTokenAndPassword(formData.resetToken(), formData.newPassword()));
    }

    @Override
    protected CompletionStage<Result> onFailedRequest(final Form<? extends ResetPasswordFormData> form, final Throwable throwable,
                                                      final Function<Form<? extends ResetPasswordFormData>, CompletionStage<Result>> onBadRequest) {
        if (throwable.getCause() instanceof NotFoundException) {
            form.reject("errors.invalidPasswordToken");
            return onBadRequest.apply(form);
        }
        return super.onFailedRequest(form, throwable, onBadRequest);
    }

    protected final CompletionStage<Customer> executeWithHooks(final CustomerPasswordResetCommand baseCommand) {
        return sphereClient.execute(CustomerPasswordResetCommandHook.runHook(hookRunner, baseCommand));
    }
}
