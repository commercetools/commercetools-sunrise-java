package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface RemoveAddressActionDataProvider<C, A, F> {

    CompletionStage<RemoveAddressActionData> getActionData(final C customerIdentifier, final A addressIdentifier, @Nullable final F form);
}
