package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultChangeAddressExecutor implements ChangeAddressExecutor {

    private final SphereClient sphereClient;
    private final HookContext hookContext;

    @Inject
    protected DefaultChangeAddressExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Customer> changeAddress(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        final CustomerUpdateCommand request = buildRequest(customer, oldAddress, formData);
        return sphereClient.execute(request)
                .thenApplyAsync(updatedCustomer -> {
                    runHookOnCustomerUpdated(updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }

    protected CustomerUpdateCommand buildRequest(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildUpdateActions(customer, oldAddress, formData);
        return CustomerUpdateCommand.of(customer, updateActions);
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.ofOldAddressToNewAddress(oldAddress, formData.toAddress()));
        updateActions.addAll(buildSetDefaultAddressActions(customer, oldAddress.getId(), formData));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildSetDefaultAddressActions(final Customer customer, final String addressId, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        buildSetDefaultAddressAction(addressId, formData.isDefaultShippingAddress(), customer.getDefaultShippingAddressId(), SetDefaultShippingAddress::of)
                .ifPresent(updateActions::add);
        buildSetDefaultAddressAction(addressId, formData.isDefaultBillingAddress(), customer.getDefaultBillingAddressId(), SetDefaultBillingAddress::of)
                .ifPresent(updateActions::add);
        return updateActions;
    }

    private Optional<UpdateAction<Customer>> buildSetDefaultAddressAction(final String addressId, final boolean isNewDefaultAddress,
                                                                          @Nullable final String defaultAddressId,
                                                                          final Function<String, UpdateAction<Customer>> actionCreator) {
        final boolean defaultNeedsChange = isDefaultAddressDifferent(addressId, isNewDefaultAddress, defaultAddressId);
        if (defaultNeedsChange) {
            final String addressIdToSetAsDefault = isNewDefaultAddress ? addressId : null;
            return Optional.of(actionCreator.apply(addressIdToSetAsDefault));
        }
        return Optional.empty();
    }

    private CompletionStage<?> runHookOnCustomerUpdated(final Customer updatedCustomer) {
        return CustomerUpdatedHook.runHook(hookContext, updatedCustomer);
    }

    protected final boolean isDefaultAddressDifferent(final String addressId, final boolean isNewDefaultAddress, @Nullable final String defaultAddressId) {
        return isNewDefaultAddress ^ isDefaultAddress(addressId, defaultAddressId);
    }

    private boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
