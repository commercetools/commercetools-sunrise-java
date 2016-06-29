package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import java.util.concurrent.CompletionStage;

public interface AddAddressActionDataProvider<C> {

    CompletionStage<AddAddressActionData> getActionData(final C customerIdentifier);
}
