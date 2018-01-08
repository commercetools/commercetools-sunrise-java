package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class DefaultAddAddressControllerAction implements AddAddressControllerAction {

    private final MyCustomerUpdater myCustomerUpdater;

    @Inject
    protected DefaultAddAddressControllerAction(final MyCustomerUpdater myCustomerUpdater) {
        this.myCustomerUpdater = myCustomerUpdater;
    }

    @Override
    public CompletionStage<Customer> apply(final AddressFormData formData) {
        return myCustomerUpdater.force(formData.updateActions())
                .thenComposeAsync(customer -> myCustomerUpdater.force(buildActionsToSetDefaults(formData, customer)),
                        HttpExecution.defaultContext());
    }

    private List<UpdateAction<Customer>> buildActionsToSetDefaults(final AddressFormData formData, final Customer customer) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        findAddressId(customer, formData.address())
                .ifPresent(addressId -> {
                    if (formData.defaultShippingAddress()) {
                        updateActions.add(SetDefaultShippingAddress.of(addressId));
                    }
                    if (formData.defaultBillingAddress()) {
                        updateActions.add(SetDefaultBillingAddress.of(addressId));
                    }
                });
        return updateActions;
    }

    protected final Optional<String> findAddressId(final Customer customer, final Address addressWithoutId) {
        return customer.getAddresses().stream()
                .filter(address -> address.equalsIgnoreId(addressWithoutId))
                .findAny()
                .map(Address::getId);
    }
}
