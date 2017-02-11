package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeEmail;
import io.sphere.sdk.customers.commands.updateactions.SetFirstName;
import io.sphere.sdk.customers.commands.updateactions.SetLastName;
import io.sphere.sdk.customers.commands.updateactions.SetTitle;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultMyPersonalDetailsUpdater implements MyPersonalDetailsUpdater {

    private final SphereClient sphereClient;
    private final HookContext hookContext;

    @Inject
    protected DefaultMyPersonalDetailsUpdater(final SphereClient sphereClient, final HookContext hookContext) {
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Customer> updateCustomer(final Customer customer, final MyPersonalDetailsFormData formData) {
        final CustomerUpdateCommand sphereRequest = buildRequest(customer, formData);
        if (!sphereRequest.getUpdateActions().isEmpty()) {
            return sphereClient.execute(sphereRequest)
                    .thenApplyAsync(updatedCustomer -> {
                        runHookOnCustomerUpdated(updatedCustomer);
                        return updatedCustomer;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(customer);
        }
    }

    protected CustomerUpdateCommand buildRequest(final Customer customer, final MyPersonalDetailsFormData formData) {
        return CustomerUpdateCommand.of(customer, buildUpdateActions(customer, formData));
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final Customer customer, final MyPersonalDetailsFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        final CustomerName customerName = formData.toCustomerName();
        if (!Objects.equals(customer.getTitle(), customerName.getTitle())) {
            updateActions.add(SetTitle.of(customerName.getTitle()));
        }
        if (!Objects.equals(customer.getFirstName(), customerName.getFirstName())) {
            updateActions.add(SetFirstName.of(customerName.getFirstName()));
        }
        if (!Objects.equals(customer.getLastName(), customerName.getLastName())) {
            updateActions.add(SetLastName.of(customerName.getLastName()));
        }
        if (!Objects.equals(customer.getEmail(), formData.getEmail())) {
            updateActions.add(ChangeEmail.of(formData.getEmail()));
        }
        return updateActions;
    }

    private CompletionStage<?> runHookOnCustomerUpdated(final Customer customer) {
        return CustomerUpdatedHook.runHook(hookContext, customer);
    }
}
