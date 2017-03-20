package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleAddressBookReverseRouterByReflection.class)
public interface SimpleAddressBookReverseRouter {

    String ADDRESS_BOOK_DETAIL_PAGE = "addressBookDetailPageCall";
    
    Call addressBookDetailPageCall(final String languageTag);

    String ADD_ADDRESS_PAGE = "addAddressPageCall";

    Call addAddressPageCall(final String languageTag);

    String ADD_ADDRESS_PROCESS = "addAddressProcessCall";

    Call addAddressProcessCall(final String languageTag);

    String CHANGE_ADDRESS_PAGE = "changeAddressPageCall";

    Call changeAddressPageCall(final String languageTag, final String addressIdentifier);

    String CHANGE_ADDRESS_PROCESS = "changeAddressProcessCall";

    Call changeAddressProcessCall(final String languageTag, final String addressIdentifier);

    String REMOVE_ADDRESS_PROCESS = "removeAddressProcessCall";

    Call removeAddressProcessCall(final String languageTag, final String addressIdentifier);
}
