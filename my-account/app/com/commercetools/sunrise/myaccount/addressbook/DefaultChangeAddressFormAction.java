package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.core.controllers.AbstractFormAction;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DefaultChangeAddressFormAction extends AbstractFormAction<ChangeAddressFormData> implements ChangeAddressFormAction {

    private final ChangeAddressFormData formData;
    private final MyCustomerUpdater myCustomerUpdater;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    protected DefaultChangeAddressFormAction(final FormFactory formFactory, final ChangeAddressFormData formData,
                                             final MyCustomerUpdater myCustomerUpdater, final MyCustomerInCache myCustomerInCache) {
        super(formFactory);
        this.formData = formData;
        this.myCustomerUpdater = myCustomerUpdater;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    protected Class<? extends ChangeAddressFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final ChangeAddressFormData formData) {
        return myCustomerInCache.require()
                .thenApply(customer -> buildUpdateActions(formData, customer))
                .thenComposeAsync(myCustomerUpdater::force, HttpExecution.defaultContext());
    }

    private List<UpdateAction<Customer>> buildUpdateActions(final ChangeAddressFormData formData, final Customer customer) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.of(formData.addressId(), formData.address()));
        updateActions.addAll(buildSetDefaultAddressActions(formData, customer));
        return updateActions;
    }

    private List<UpdateAction<Customer>> buildSetDefaultAddressActions(final ChangeAddressFormData formData, final Customer customer) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        buildSetDefaultAddressAction(formData.addressId(), formData.defaultShippingAddress(), customer.getDefaultShippingAddressId(), SetDefaultShippingAddress::of)
                .ifPresent(updateActions::add);
        buildSetDefaultAddressAction(formData.addressId(), formData.defaultBillingAddress(), customer.getDefaultBillingAddressId(), SetDefaultBillingAddress::of)
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

    private boolean isDefaultAddressDifferent(final String addressId, final boolean isNewDefaultAddress, @Nullable final String defaultAddressId) {
        return isNewDefaultAddress ^ isDefaultAddress(addressId, defaultAddressId);
    }

    private boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
