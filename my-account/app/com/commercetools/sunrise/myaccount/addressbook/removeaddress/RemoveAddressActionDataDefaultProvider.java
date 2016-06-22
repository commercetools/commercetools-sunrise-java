package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public final class RemoveAddressActionDataDefaultProvider implements RemoveAddressActionDataProvider<Http.Session, String, Form<RemoveAddressFormData>> {

    @Inject
    CustomerFinderBySession customerFinderBySession;

    @Override
    public CompletionStage<RemoveAddressActionData> getActionData(final Http.Session session, final String addressId,
                                                                  @Nullable final Form<RemoveAddressFormData> form) {
        return customerFinderBySession.findCustomer(session)
                .thenApplyAsync(customerOpt -> customerOpt
                        .map(customer -> findAddress(customer, addressId)
                                .map(address -> RemoveAddressActionData.of(customer, form, address))
                                .orElseGet(() -> RemoveAddressActionData.ofNotFoundAddress(customer)))
                        .orElseGet(RemoveAddressActionData::ofNotFoundCustomer));
    }

    private Optional<Address> findAddress(final Customer customer, final String addressId) {
        return customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findFirst();
    }
}