package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.reverserouter.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookLocalizedReverseRouter.class)
public interface AddressBookReverseRouter extends AddressBookSimpleReverseRouter, LocalizedReverseRouter {

    default Call addressBookCall() {
        return addressBookCall(languageTag());
    }

    default Call addAddressToAddressBookCall() {
        return addAddressToAddressBookCall(languageTag());
    }

    default Call addAddressToAddressBookProcessFormCall() {
        return addAddressToAddressBookProcessFormCall(languageTag());
    }

    default Call changeAddressInAddressBookCall(final String addressId) {
        return changeAddressInAddressBookCall(languageTag(), addressId);
    }

    default Call changeAddressInAddressBookProcessFormCall(final String addressId) {
        return changeAddressInAddressBookProcessFormCall(languageTag(), addressId);
    }

    default Call removeAddressFromAddressBookProcessFormCall(final String addressId) {
        return removeAddressFromAddressBookProcessFormCall(languageTag(), addressId);
    }
}
