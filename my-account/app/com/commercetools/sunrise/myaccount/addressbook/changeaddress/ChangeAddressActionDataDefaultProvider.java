package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressFinderById;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressFormData;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class ChangeAddressActionDataDefaultProvider implements ChangeAddressActionDataProvider<Http.Session, String, Form<DefaultAddressFormData>> {

    @Inject
    AddressFinderById addressFinderById;

    @Override
    public CompletionStage<ChangeAddressActionData> getActionData(final Http.Session session, final String addressId,
                                                                  @Nullable final Form<DefaultAddressFormData> form) {
        return addressFinderById.findAddress(session, addressId)
                .thenApplyAsync(addressFinderResult -> addressFinderResult.getCustomer()
                        .map(customer -> addressFinderResult.getAddress()
                                .map(oldAddress -> ChangeAddressActionData.of(customer, form, oldAddress))
                                .orElseGet(() -> ChangeAddressActionData.ofNotFoundAddress(customer)))
                        .orElseGet(ChangeAddressActionData::ofNotFoundCustomer));
    }
}