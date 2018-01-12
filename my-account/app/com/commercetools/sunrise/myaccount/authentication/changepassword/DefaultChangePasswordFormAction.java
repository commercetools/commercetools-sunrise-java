package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerChangedPasswordHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerChangePasswordCommandHook;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isCustomerInvalidCurrentPasswordError;

final class DefaultChangePasswordFormAction extends AbstractFormAction<ChangePasswordFormData> implements ChangePasswordFormAction {

    private final ChangePasswordFormData formData;
    private final SphereClient sphereClient;
    private final HookRunner hookRunner;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    DefaultChangePasswordFormAction(final FormFactory formFactory, final ChangePasswordFormData formData,
                                    final SphereClient sphereClient, final HookRunner hookRunner,
                                    final MyCustomerInCache myCustomerInCache) {
        super(formFactory);
        this.formData = formData;
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    protected Class<? extends ChangePasswordFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ChangePasswordFormData formData) {
        return myCustomerInCache.require()
                .thenApply(customer -> CustomerChangePasswordCommand.of(customer, formData.oldPassword(), formData.newPassword()))
                .thenComposeAsync(this::executeWithHooks, HttpExecution.defaultContext());
    }

    @Override
    protected CompletionStage<Result> onFailedRequest(final Form<? extends ChangePasswordFormData> form, final Throwable throwable,
                                                      final Function<Form<? extends ChangePasswordFormData>, CompletionStage<Result>> onBadRequest) {
        if (isCustomerInvalidCurrentPasswordError(throwable.getCause())) {
            form.reject("errors.invalidCurrentPassword");
            return onBadRequest.apply(form);
        }
        return super.onFailedRequest(form, throwable, onBadRequest);
    }

    protected final CompletionStage<Customer> executeWithHooks(final CustomerChangePasswordCommand baseCommand) {
        final CustomerChangePasswordCommand command = CustomerChangePasswordCommandHook.runHook(hookRunner, baseCommand);
        return sphereClient.execute(command)
                .thenApplyAsync(updatedCustomer -> {
                    CustomerChangedPasswordHook.runHook(hookRunner, updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }
}
