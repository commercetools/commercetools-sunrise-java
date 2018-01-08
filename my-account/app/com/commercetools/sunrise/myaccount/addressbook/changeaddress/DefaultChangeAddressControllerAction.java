package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.commercetools.sunrise.models.customers.AbstractCustomerUpdateExecutor;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
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

public class DefaultChangeAddressControllerAction extends AbstractCustomerUpdateExecutor implements ChangeAddressControllerAction {

    private final MyCustomerUpdater myCustomerUpdater;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    protected DefaultChangeAddressControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                   final MyCustomerUpdater myCustomerUpdater, final MyCustomerInCache myCustomerInCache) {
        super(sphereClient, hookRunner);
        this.myCustomerUpdater = myCustomerUpdater;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public CompletionStage<Customer> apply(final String addressId, final AddressFormData formData) {
        return myCustomerInCache.require()
                .thenComposeAsync(customer -> myCustomerUpdater.force(buildUpdateActions(addressId, formData, customer)),
                        HttpExecution.defaultContext());
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final String addressId, final AddressFormData formData, final Customer customer) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.of(addressId, formData.address()));
        updateActions.addAll(buildSetDefaultAddressActions(addressId, formData, customer));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildSetDefaultAddressActions(final String addressId, final AddressFormData formData, final Customer customer) {
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
