package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveAddressControllerAction extends AbstractCustomerUpdateExecutor implements RemoveAddressControllerAction {

    @Inject
    protected DefaultRemoveAddressControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final RemoveAddressFormData formData) {
        return executeRequest(addressWithCustomer.getCustomer(), buildRequest(addressWithCustomer, formData));
    }

    protected CustomerUpdateCommand buildRequest(final AddressWithCustomer actionData, final RemoveAddressFormData formData) {
        final RemoveAddress updateAction = RemoveAddress.of(actionData.getAddress());
        return CustomerUpdateCommand.of(actionData.getCustomer(), updateAction);
    }
}
