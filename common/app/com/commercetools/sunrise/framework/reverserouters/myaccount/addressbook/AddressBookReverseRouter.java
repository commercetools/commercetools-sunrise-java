package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(DefaultAddressBookReverseRouter.class)
public interface AddressBookReverseRouter extends SimpleAddressBookReverseRouter, LocalizedReverseRouter {

    default Call addressBookDetailPageCall() {
        return addressBookDetailPageCall(locale().toLanguageTag());
    }

    default Call addAddressPageCall() {
        return addAddressPageCall(locale().toLanguageTag());
    }

    default Call addAddressProcessCall() {
        return addAddressProcessCall(locale().toLanguageTag());
    }

    default Call changeAddressPageCall(final String addressIdentifier) {
        return changeAddressPageCall(locale().toLanguageTag(), addressIdentifier);
    }

    default Call changeAddressProcessCall(final String addressIdentifier) {
        return changeAddressProcessCall(locale().toLanguageTag(), addressIdentifier);
    }

    default Call removeAddressProcessCall(final String addressIdentifier) {
        return removeAddressProcessCall(locale().toLanguageTag(), addressIdentifier);
    }

    /**
     * Finds the call to access the edit form page for the given address.
     * @param address the address that we want to edit via the change address form page call
     * @return the page call to access the edit form page of the given address.
     */
    Optional<Call> changeAddressPageCall(final Address address);

    /**
     * Finds the call to process the edit form page for the given address.
     * @param address the address that we want to edit via the page call
     * @return the page call to process the edit form page of the given address.
     */
    Optional<Call> changeAddressProcessCall(final Address address);

    /**
     * Finds the call to process the deletion of the given address.
     * @param address the address that we want to delete via the page call
     * @return the page call to process the deletion of the given address.
     */
    Optional<Call> removeAddressProcessCall(final Address address);
}
