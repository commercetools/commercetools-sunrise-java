package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(DefaultAddressBookReverseRouter.class)
public interface AddressBookReverseRouter extends ReverseRouter {

    String ADDRESS_BOOK_DETAIL_PAGE = "addressBookDetailPageCall";

    Call addressBookDetailPageCall();

    String ADD_ADDRESS_PAGE = "addAddressPageCall";

    Call addAddressPageCall();

    String ADD_ADDRESS_PROCESS = "addAddressProcessCall";

    Call addAddressProcessCall();

    String CHANGE_ADDRESS_PAGE = "changeAddressPageCall";

    Call changeAddressPageCall(final String addressIdentifier);

    String CHANGE_ADDRESS_PROCESS = "changeAddressProcessCall";

    Call changeAddressProcessCall(final String addressIdentifier);

    String REMOVE_ADDRESS_PROCESS = "removeAddressProcessCall";

    Call removeAddressProcessCall(final String addressIdentifier);

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
