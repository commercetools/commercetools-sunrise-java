package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class AddAddressActionDataDefaultProvider implements AddAddressActionDataProvider<Http.Session> {

    @Inject
    CustomerFinderBySession customerFinderBySession;

    @Override
    public CompletionStage<AddAddressActionData> getActionData(final Http.Session session) {
        return customerFinderBySession.findCustomer(session)
                .thenApplyAsync(customerOpt -> customerOpt
                        .map(AddAddressActionData::of)
                        .orElseGet(AddAddressActionData::ofNotFoundCustomer));
    }
}