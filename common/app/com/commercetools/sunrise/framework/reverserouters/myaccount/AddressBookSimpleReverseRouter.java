package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookReverseRouter.class)
interface AddressBookSimpleReverseRouter {

    String ADDRESS_BOOK_PAGE = "addressBookPageCall";
    
    Call addressBookPageCall(final String languageTag);

    String ADD_ADDRESS_PAGE = "addAddressPageCall";

    Call addAddressPageCall(final String languageTag);

    String ADD_ADDRESS_PROCESS = "addAddressProcessCall";

    Call addAddressProcessCall(final String languageTag);

    String CHANGE_ADDRESS_PAGE = "changeAddressPageCall";

    Call changeAddressPageCall(final String languageTag, final String addressId);

    String CHANGE_ADDRESS_PROCESS = "changeAddressProcessCall";

    Call changeAddressProcessCall(final String languageTag, final String addressId);

    String REMOVE_ADDRESS_PROCESS = "removeAddressProcessCall";

    Call removeAddressProcessCall(final String languageTag, final String addressId);
}
