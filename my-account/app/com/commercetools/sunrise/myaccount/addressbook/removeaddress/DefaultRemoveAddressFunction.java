package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.myaccount.AbstractCustomerUpdater;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveAddressFunction extends AbstractCustomerUpdater implements RemoveAddressFunction {

    @Inject
    protected DefaultRemoveAddressFunction(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
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
