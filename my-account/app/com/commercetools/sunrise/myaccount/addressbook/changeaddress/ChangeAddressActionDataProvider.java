package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface ChangeAddressActionDataProvider<C, A, F> {

    CompletionStage<ChangeAddressActionData> getActionData(final C customerIdentifier, final A addressIdentifier, @Nullable final F form);
}
