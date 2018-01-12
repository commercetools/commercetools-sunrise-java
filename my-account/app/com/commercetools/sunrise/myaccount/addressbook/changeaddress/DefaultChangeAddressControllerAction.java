package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.core.controllers.AbstractControllerAction;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import com.commercetools.sunrise.models.customers.MyCustomerUpdater;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import play.data.Form;
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

public class DefaultChangeAddressControllerAction extends AbstractControllerAction<ChangeAddressFormData> implements ChangeAddressControllerAction {


    private final ChangeAddressFormData formData;
    private final MyCustomerUpdater myCustomerUpdater;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    protected DefaultChangeAddressControllerAction(final FormFactory formFactory, final ChangeAddressFormData formData,
                                                   final MyCustomerUpdater myCustomerUpdater, final MyCustomerInCache myCustomerInCache) {
        super(formFactory, templateEngine);
        this.formData = formData;
        this.myCustomerUpdater = myCustomerUpdater;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public Form<? extends ChangeAddressFormData> bindForm() {
        return bindForm(formData.getClass());
    }

    @Override
    public CompletionStage<?> apply(final ChangeAddressFormData formData) {
        return myCustomerInCache.require()
                .thenComposeAsync(customer -> myCustomerUpdater.force(buildUpdateActions(formData, customer)),
                        HttpExecution.defaultContext());
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

    protected final boolean isDefaultAddressDifferent(final String addressId, final boolean isNewDefaultAddress, @Nullable final String defaultAddressId) {
        return isNewDefaultAddress ^ isDefaultAddress(addressId, defaultAddressId);
    }

    private boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
