package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultAddAddressExecutor implements AddAddressExecutor {

    private final SphereClient sphereClient;

    @Inject
    protected DefaultAddAddressExecutor(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Customer> addAddress(final Customer customer, final AddressBookAddressFormData formData) {
        return sphereClient.execute(buildRequest(customer, formData))
                .thenComposeAsync(updatedCustomer ->
                        setAddressAsDefault(updatedCustomer, formData),
                        HttpExecution.defaultContext());
    }

    private CompletionStage<Customer> setAddressAsDefault(final Customer customer, final AddressBookAddressFormData formData) {
        final CustomerUpdateCommand command = buildPostRequest(customer, formData);
        if (!command.getUpdateActions().isEmpty()) {
            return sphereClient.execute(command);
        } else {
            return completedFuture(customer);
        }
    }

    protected CustomerUpdateCommand buildRequest(final Customer customer, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildUpdateActions(formData);
        return CustomerUpdateCommand.of(customer, updateActions);
    }

    protected CustomerUpdateCommand buildPostRequest(final Customer customer, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildPostUpdateActions(customer, formData);
        return CustomerUpdateCommand.of(customer, updateActions);
    }

    protected final Optional<String> findAddressId(final Customer customer, final Address addressWithoutId) {
        return customer.getAddresses().stream()
                .filter(address -> address.equalsIgnoreId(addressWithoutId))
                .findFirst()
                .map(Address::getId);
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(AddAddress.of(formData.toAddress()));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildPostUpdateActions(final Customer customer, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        findAddressId(customer, formData.toAddress())
                .ifPresent(addressId -> {
                    if (formData.isDefaultShippingAddress()) {
                        updateActions.add(SetDefaultShippingAddress.of(addressId));
                    }
                    if (formData.isDefaultBillingAddress()) {
                        updateActions.add(SetDefaultBillingAddress.of(addressId));
                    }
                });
        return updateActions;
    }
}
