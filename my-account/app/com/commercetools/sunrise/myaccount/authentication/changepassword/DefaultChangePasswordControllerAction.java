package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.AbstractControllerAction;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultChangePasswordControllerAction extends AbstractControllerAction<ChangePasswordFormData> implements ChangePasswordControllerAction {

    private final MyCustomerInCache myCustomerInCache;
    private final SphereClient sphereClient;

    @Inject
    DefaultChangePasswordControllerAction(final FormFactory formFactory, final MyCustomerInCache myCustomerInCache,
                                          final SphereClient sphereClient) {
        super(formFactory, templateEngine);
        this.myCustomerInCache = myCustomerInCache;
        this.sphereClient = sphereClient;
    }

    @Override
    public Form<? extends ChangePasswordFormData> bindForm() {
        return bindForm(ChangePasswordFormData.class);
    }

    @Override
    public CompletionStage<Void> apply(final ChangePasswordFormData formData) {
        return myCustomerInCache.require()
                .thenAcceptAsync(customer -> executeRequest(formData, customer), HttpExecution.defaultContext());
    }

    private CompletionStage<Customer> executeRequest(final ChangePasswordFormData formData, final Customer customer) {
        return sphereClient.execute(CustomerChangePasswordCommand.of(customer, formData.oldPassword(), formData.newPassword()));
    }
}
