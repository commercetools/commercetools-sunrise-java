package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressFormData;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public final class ChangeAddressActionDataDefaultProvider implements ChangeAddressActionDataProvider<Http.Session, String, Form<DefaultAddressFormData>> {

    @Inject
    CustomerFinderBySession customerFinderBySession;

    @Override
    public CompletionStage<ChangeAddressActionData> getActionData(final Http.Session session, final String addressId,
                                                                  @Nullable final Form<DefaultAddressFormData> form) {
        return customerFinderBySession.findCustomer(session)
                .thenApplyAsync(customerOpt -> customerOpt
                        .map(customer -> findAddress(customer, addressId)
                                .map(address -> ChangeAddressActionData.of(customer, form, address))
                                .orElseGet(() -> ChangeAddressActionData.ofNotFoundAddress(customer)))
                        .orElseGet(ChangeAddressActionData::ofNotFoundCustomer));
    }

    private Optional<Address> findAddress(final Customer customer, final String addressId) {
        return customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findFirst();
    }
}