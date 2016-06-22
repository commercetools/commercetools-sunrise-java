package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface AddressBookReverseRouter {

    Call showMyAddressBook(final String languageTag);

    Call showAddAddressToMyAddressBook(final String languageTag);

    Call processAddAddressToMyAddressBookForm(final String languageTag);

    Call showChangeAddressInMyAddressBook(final String languageTag, final String addressId);

    Call processChangeAddressInMyAddressBookForm(final String languageTag, final String addressId);

    Call processRemoveAddressFromMyAddressBookForm(final String languageTag, final String addressId);

}
