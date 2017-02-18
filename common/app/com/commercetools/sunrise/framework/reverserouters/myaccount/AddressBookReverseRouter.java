package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookLocalizedReverseRouter.class)
public interface AddressBookReverseRouter extends AddressBookSimpleReverseRouter, LocalizedReverseRouter {

    default Call addressBookPageCall() {
        return addressBookPageCall(languageTag());
    }

    default Call addAddressPageCall() {
        return addAddressPageCall(languageTag());
    }

    default Call addAddressProcessCall() {
        return addAddressProcessCall(languageTag());
    }

    default Call changeAddressPageCall(final String addressId) {
        return changeAddressPageCall(languageTag(), addressId);
    }

    default Call changeAddressProcessCall(final String addressId) {
        return changeAddressProcessCall(languageTag(), addressId);
    }

    default Call removeAddressProcessCall(final String addressId) {
        return removeAddressProcessCall(languageTag(), addressId);
    }
}
