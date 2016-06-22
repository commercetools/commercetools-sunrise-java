package com.commercetools.sunrise.myaccount.addressbook;

import java.util.concurrent.CompletionStage;

public interface AddressFinder<C, I> {

    CompletionStage<AddressFinderResult> findAddress(final C customerIdentifier, final I addressIdentifier);
}
