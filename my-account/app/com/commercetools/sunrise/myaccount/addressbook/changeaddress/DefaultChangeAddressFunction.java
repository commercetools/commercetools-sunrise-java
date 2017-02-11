package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultChangeAddressFunction implements ChangeAddressFunction {

    private final SphereClient sphereClient;
    private final HookContext hookContext;

    @Inject
    protected DefaultChangeAddressFunction(final SphereClient sphereClient, final HookContext hookContext) {
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final AddressBookAddressFormData formData) {
        final CustomerUpdateCommand request = buildRequest(addressWithCustomer, formData);
        return sphereClient.execute(request)
                .thenApplyAsync(updatedCustomer -> {
                    runHookOnCustomerUpdated(updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }

    protected CustomerUpdateCommand buildRequest(final AddressWithCustomer addressWithCustomer, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildUpdateActions(addressWithCustomer, formData);
        return CustomerUpdateCommand.of(addressWithCustomer.getCustomer(), updateActions);
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final AddressWithCustomer addressWithCustomer, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.ofOldAddressToNewAddress(addressWithCustomer.getAddress(), formData.toAddress()));
        updateActions.addAll(buildSetDefaultAddressActions(addressWithCustomer.getCustomer(), addressWithCustomer.getAddress().getId(), formData));
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
