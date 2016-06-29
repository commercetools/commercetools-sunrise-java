package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import java.util.concurrent.CompletionStage;

public interface ChangeAddressActionDataProvider<C, A> {

    CompletionStage<ChangeAddressActionData> getActionData(final C customerIdentifier, final A addressIdentifier);
}
