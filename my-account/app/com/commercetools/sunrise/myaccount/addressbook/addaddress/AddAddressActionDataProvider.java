package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface AddAddressActionDataProvider<C, F> {

    CompletionStage<AddAddressActionData> getActionData(final C customerIdentifier, @Nullable final F form);
}
