package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.AbstractCustomerUpdateExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeEmail;
import io.sphere.sdk.customers.commands.updateactions.SetFirstName;
import io.sphere.sdk.customers.commands.updateactions.SetLastName;
import io.sphere.sdk.customers.commands.updateactions.SetTitle;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public class DefaultMyPersonalDetailsControllerAction extends AbstractCustomerUpdateExecutor implements MyPersonalDetailsControllerAction {

    @Inject
    protected DefaultMyPersonalDetailsControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final Customer customer, final MyPersonalDetailsFormData formData) {
        return executeRequest(customer, buildRequest(customer, formData));
    }

    protected CustomerUpdateCommand buildRequest(final Customer customer, final MyPersonalDetailsFormData formData) {
        return CustomerUpdateCommand.of(customer, buildUpdateActions(customer, formData));
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final Customer customer, final MyPersonalDetailsFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        final CustomerName customerName = formData.customerName();
        if (!Objects.equals(customer.getTitle(), customerName.getTitle())) {
            updateActions.add(SetTitle.of(customerName.getTitle()));
        }
        if (!Objects.equals(customer.getFirstName(), customerName.getFirstName())) {
            updateActions.add(SetFirstName.of(customerName.getFirstName()));
        }
        if (!Objects.equals(customer.getLastName(), customerName.getLastName())) {
            updateActions.add(SetLastName.of(customerName.getLastName()));
        }
        if (!Objects.equals(customer.getEmail(), formData.email())) {
            updateActions.add(ChangeEmail.of(formData.email()));
        }
        return updateActions;
    }
}
