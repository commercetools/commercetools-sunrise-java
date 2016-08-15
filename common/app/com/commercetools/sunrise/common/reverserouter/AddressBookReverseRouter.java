package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookReverseRouter.class)
public interface AddressBookReverseRouter {

    Call addressBookCall(final String languageTag);

    Call addAddressToAddressBookCall(final String languageTag);

    Call addAddressToAddressBookProcessFormCall(final String languageTag);

    Call changeAddressInAddressBookCall(final String languageTag, final String addressId);

    Call changeAddressInAddressBookProcessFormCall(final String languageTag, final String addressId);

    Call removeAddressFromAddressBookProcessFormCall(final String languageTag, final String addressId);

}
