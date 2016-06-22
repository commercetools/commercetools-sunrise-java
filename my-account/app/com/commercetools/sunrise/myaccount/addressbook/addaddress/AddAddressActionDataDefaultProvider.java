package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressFormData;
import play.data.Form;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class AddAddressActionDataDefaultProvider implements AddAddressActionDataProvider<Http.Session, Form<DefaultAddressFormData>> {

    @Inject
    CustomerFinderBySession customerFinderBySession;

    @Override
    public CompletionStage<AddAddressActionData> getActionData(final Http.Session session, @Nullable final Form<DefaultAddressFormData> form) {
        return customerFinderBySession.findCustomer(session)
                .thenApplyAsync(customerOpt -> customerOpt
                        .map(customer -> AddAddressActionData.of(customer, form))
                        .orElseGet(AddAddressActionData::ofNotFoundCustomer));
    }
}