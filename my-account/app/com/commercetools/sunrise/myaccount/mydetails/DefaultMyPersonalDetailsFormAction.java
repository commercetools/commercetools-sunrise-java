package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerName;
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

public class DefaultMyPersonalDetailsFormAction extends AbstractCustomerUpdateExecutor implements MyPersonalDetailsFormAction {

    private final MyCustomerUpdater myCustomerUpdater;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    protected DefaultMyPersonalDetailsFormAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                 final MyCustomerUpdater myCustomerUpdater,
                                                 final MyCustomerInCache myCustomerInCache) {
        super(sphereClient, hookRunner);
        this.myCustomerUpdater = myCustomerUpdater;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public CompletionStage<Customer> apply(final MyPersonalDetailsFormData formData) {
        return myCustomerInCache.require()
                .thenApply(customer -> buildUpdateActions(formData, customer))
                .thenComposeAsync(myCustomerUpdater::force, HttpExecution.defaultContext());
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final MyPersonalDetailsFormData formData, final Customer customer) {
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
