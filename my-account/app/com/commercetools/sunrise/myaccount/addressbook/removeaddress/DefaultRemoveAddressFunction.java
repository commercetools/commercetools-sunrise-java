package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveAddressFunction implements RemoveAddressFunction {

    private final SphereClient sphereClient;
    private final HookContext hookContext;

    @Inject
    protected DefaultRemoveAddressFunction(final SphereClient sphereClient, final HookContext hookContext) {
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Customer> apply(final RemoveAddressFormData formData, final AddressWithCustomer actionData) {
        final CustomerUpdateCommand request = buildRequest(actionData, formData);
        return sphereClient.execute(request)
                .thenApplyAsync(updatedCustomer -> {
                    CustomerUpdatedHook.runHook(hookContext, updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }

    protected CustomerUpdateCommand buildRequest(final AddressWithCustomer actionData, final RemoveAddressFormData formData) {
        final RemoveAddress updateAction = RemoveAddress.of(actionData.getAddress());
        return CustomerUpdateCommand.of(actionData.getCustomer(), updateAction);
    }
}
