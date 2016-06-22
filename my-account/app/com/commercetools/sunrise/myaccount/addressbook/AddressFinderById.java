package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public final class AddressFinderById implements AddressFinder<Http.Session, String> {

    @Inject
    CustomerFinderBySession customerFinderBySession;

    @Override
    public CompletionStage<AddressFinderResult> findAddress(final Http.Session session, final String addressId) {
        return customerFinderBySession.findCustomer(session)
                .thenApplyAsync(customerOpt -> customerOpt
                        .map(customer -> buildResult(customer, addressId))
                        .orElseGet(AddressFinderResult::ofNotFoundCustomer));
    }

    private AddressFinderResult buildResult(final Customer customer, final String addressId) {
        final Address address = customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findFirst().orElse(null);
        return AddressFinderResult.of(customer, address);
    }
}