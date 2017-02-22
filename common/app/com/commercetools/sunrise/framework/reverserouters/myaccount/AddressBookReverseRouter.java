package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookLocalizedReverseRouter.class)
public interface AddressBookReverseRouter extends AddressBookSimpleReverseRouter, LocalizedReverseRouter {

    default Call addressBookDetailPageCall() {
        return addressBookDetailPageCall(languageTag());
    }

    default Call addAddressPageCall() {
        return addAddressPageCall(languageTag());
    }

    default Call addAddressProcessCall() {
        return addAddressProcessCall(languageTag());
    }

    default Call changeAddressPageCall(final String addressIdentifier) {
        return changeAddressPageCall(languageTag(), addressIdentifier);
    }

    default Call changeAddressProcessCall(final String addressIdentifier) {
        return changeAddressProcessCall(languageTag(), addressIdentifier);
    }

    default Call removeAddressProcessCall(final String addressIdentifier) {
        return removeAddressProcessCall(languageTag(), addressIdentifier);
    }
}
