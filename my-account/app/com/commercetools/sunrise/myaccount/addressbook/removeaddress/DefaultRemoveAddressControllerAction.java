package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveAddressControllerAction extends AbstractCustomerUpdateExecutor implements RemoveAddressControllerAction {

    private final MyCustomerUpdater myCustomerUpdater;

    @Inject
    protected DefaultRemoveAddressControllerAction(final SphereClient sphereClient, final HookRunner hookRunner, final MyCustomerUpdater myCustomerUpdater) {
        super(sphereClient, hookRunner);
        this.myCustomerUpdater = myCustomerUpdater;
    }

    @Override
    public CompletionStage<Customer> apply(final RemoveAddressFormData formData) {
        return myCustomerUpdater.force(RemoveAddress.of(formData.addressId()));
    }
}
