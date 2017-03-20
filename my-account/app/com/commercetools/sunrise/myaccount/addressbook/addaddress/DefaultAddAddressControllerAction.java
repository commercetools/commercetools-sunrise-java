package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
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

public class DefaultAddAddressControllerAction extends AbstractCustomerUpdateExecutor implements AddAddressControllerAction {

    @Inject
    protected DefaultAddAddressControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final Customer customer, final AddressFormData formData) {
        return executeRequest(customer, buildRequest(customer, formData))
                .thenComposeAsync(updatedCustomer -> executeRequest(updatedCustomer, buildPostRequest(updatedCustomer, formData)), HttpExecution.defaultContext());
    }

    protected CustomerUpdateCommand buildRequest(final Customer customer, final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildUpdateActions(formData);
        return CustomerUpdateCommand.of(customer, updateActions);
    }

    protected CustomerUpdateCommand buildPostRequest(final Customer customer, final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildPostUpdateActions(customer, formData);
        return CustomerUpdateCommand.of(customer, updateActions);
    }

    protected final Optional<String> findAddressId(final Customer customer, final Address addressWithoutId) {
        return customer.getAddresses().stream()
                .filter(address -> address.equalsIgnoreId(addressWithoutId))
                .findFirst()
                .map(Address::getId);
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(AddAddress.of(formData.address()));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildPostUpdateActions(final Customer customer, final AddressFormData formData) {
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
}
