package com.commercetools.sunrise.myaccount.changepassword;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerPasswordUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerChangePasswordCommandHook;
import com.commercetools.sunrise.models.customers.MyCustomer;
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
    private final MyCustomer myCustomer;

    @Inject
    DefaultChangePasswordFormAction(final FormFactory formFactory, final ChangePasswordFormData formData,
                                    final SphereClient sphereClient, final HookRunner hookRunner,
                                    final MyCustomer myCustomer) {
        super(formFactory);
        this.formData = formData;
        this.sphereClient = sphereClient;
        this.hookRunner = hookRunner;
        this.myCustomer = myCustomer;
    }

    @Override
    protected Class<? extends ChangePasswordFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ChangePasswordFormData formData) {
        return myCustomer.require()
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
        final CompletionStage<Customer> resultStage = CustomerChangePasswordCommandHook.runHook(hookRunner, baseCommand)
                .thenCompose(sphereClient::execute);
        resultStage.thenAcceptAsync(resource -> CustomerUpdatedHook.runHook(hookRunner, resource), HttpExecution.defaultContext());
        return resultStage;
    }
}
