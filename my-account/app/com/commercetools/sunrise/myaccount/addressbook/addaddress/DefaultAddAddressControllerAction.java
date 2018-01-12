package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.core.controllers.AbstractControllerAction;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

public class DefaultAddAddressControllerAction extends AbstractControllerAction<AddAddressFormData> implements AddAddressControllerAction {

    private final MyCustomerUpdater myCustomerUpdater;
    private final AddAddressFormData formData;

    @Inject
    protected DefaultAddAddressControllerAction(final FormFactory formFactory, final AddAddressFormData formData, final MyCustomerUpdater myCustomerUpdater) {
        super(formFactory, templateEngine);
        this.formData = formData;
        this.myCustomerUpdater = myCustomerUpdater;
    }

    @Override
    public Form<? extends AddAddressFormData> bindForm() {
        return bindForm(formData.getClass());
    }

    @Override
    public CompletionStage<?> apply(final AddAddressFormData formData) {
        return myCustomerUpdater.force(buildUpdateActions(formData))
                .thenComposeAsync(customer -> myCustomerUpdater.force(buildUpdateActionsToSetDefaults(formData, customer)), HttpExecution.defaultContext());
    }

    protected List<? extends UpdateAction<Customer>> buildUpdateActions(final AddAddressFormData formData) {
        return singletonList(AddAddress.of(formData.address()));
    }

    protected List<? extends UpdateAction<Customer>> buildUpdateActionsToSetDefaults(final AddAddressFormData formData, final Customer customer) {
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

    private Optional<String> findAddressId(final Customer customer, final Address addressWithoutId) {
        return customer.getAddresses().stream()
                .filter(address -> address.equalsIgnoreId(addressWithoutId))
                .findAny()
                .map(Address::getId);
    }
}
