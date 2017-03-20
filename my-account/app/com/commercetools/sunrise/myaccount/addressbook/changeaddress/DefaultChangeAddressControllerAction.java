package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.myaccount.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultChangeAddressControllerAction extends AbstractCustomerUpdateExecutor implements ChangeAddressControllerAction {

    @Inject
    protected DefaultChangeAddressControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final AddressFormData formData) {
        return executeRequest(addressWithCustomer.getCustomer(), buildRequest(addressWithCustomer, formData));
    }

    protected CustomerUpdateCommand buildRequest(final AddressWithCustomer addressWithCustomer, final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = buildUpdateActions(addressWithCustomer, formData);
        return CustomerUpdateCommand.of(addressWithCustomer.getCustomer(), updateActions);
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final AddressWithCustomer addressWithCustomer, final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.ofOldAddressToNewAddress(addressWithCustomer.getAddress(), formData.address()));
        updateActions.addAll(buildSetDefaultAddressActions(addressWithCustomer.getCustomer(), addressWithCustomer.getAddress().getId(), formData));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildSetDefaultAddressActions(final Customer customer, final String addressId, final AddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        buildSetDefaultAddressAction(addressId, formData.defaultShippingAddress(), customer.getDefaultShippingAddressId(), SetDefaultShippingAddress::of)
                .ifPresent(updateActions::add);
        buildSetDefaultAddressAction(addressId, formData.defaultBillingAddress(), customer.getDefaultBillingAddressId(), SetDefaultBillingAddress::of)
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

    protected final boolean isDefaultAddressDifferent(final String addressId, final boolean isNewDefaultAddress, @Nullable final String defaultAddressId) {
        return isNewDefaultAddress ^ isDefaultAddress(addressId, defaultAddressId);
    }

    private boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
