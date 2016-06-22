package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressFinderById;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class RemoveAddressActionDataDefaultProvider implements RemoveAddressActionDataProvider<Http.Session, String, Form<RemoveAddressFormData>> {

    @Inject
    AddressFinderById addressFinderById;

    @Override
    public CompletionStage<RemoveAddressActionData> getActionData(final Http.Session session, final String addressId,
                                                                  @Nullable final Form<RemoveAddressFormData> form) {
        return addressFinderById.findAddress(session, addressId)
                .thenApplyAsync(addressFinderResult -> addressFinderResult.getCustomer()
                        .map(customer -> addressFinderResult.getAddress()
                                .map(address -> RemoveAddressActionData.of(customer, form, address))
                                .orElseGet(() -> RemoveAddressActionData.ofNotFoundAddress(customer)))
                        .orElseGet(RemoveAddressActionData::ofNotFoundCustomer));
    }
}