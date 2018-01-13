package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.core.AbstractFormAction;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class DefaultAddAddressFormAction extends AbstractFormAction<AddAddressFormData> implements AddAddressFormAction {

    private final AddAddressFormData formData;
    private final MyCustomerUpdater myCustomerUpdater;

    @Inject
    protected DefaultAddAddressFormAction(final FormFactory formFactory, final AddAddressFormData formData,
                                          final MyCustomerUpdater myCustomerUpdater) {
        super(formFactory);
        this.formData = formData;
        this.myCustomerUpdater = myCustomerUpdater;
    }

    @Override
    protected Class<? extends AddAddressFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected CompletionStage<?> onValidForm(final AddAddressFormData formData) {
        return myCustomerUpdater.force(formData.addAddress())
                .thenComposeAsync(customer -> setDefaultAddresses(formData, customer), HttpExecution.defaultContext());
    }

    private CompletionStage<Customer> setDefaultAddresses(final AddAddressFormData formData, final Customer customer) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        findAddressId(customer, formData.addAddress().getAddress())
                .ifPresent(addressId -> {
                    if (formData.defaultShippingAddress()) {
                        updateActions.add(SetDefaultShippingAddress.of(addressId));
                    }
                    if (formData.defaultBillingAddress()) {
                        updateActions.add(SetDefaultBillingAddress.of(addressId));
                    }
                });
        return myCustomerUpdater.force(updateActions);
    }

    private Optional<String> findAddressId(final Customer customer, final Address addressWithoutId) {
        return customer.getAddresses().stream()
                .filter(address -> address.equalsIgnoreId(addressWithoutId))
                .findAny()
                .map(Address::getId);
    }
}
