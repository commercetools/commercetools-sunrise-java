package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAddressBookReverseRouter.class)
interface AddressBookSimpleReverseRouter {

    String ADDRESS_BOOK_PAGE = "addressBookPage";
    
    Call addressBookPageCall(final String languageTag);

    String ADD_ADDRESS_PAGE = "addAddressPage";

    Call addAddressPageCall(final String languageTag);

    String ADD_ADDRESS_PROCESS = "addAddressProcess";

    Call addAddressProcessCall(final String languageTag);

    String CHANGE_ADDRESS_PAGE = "changeAddressPage";

    Call changeAddressPageCall(final String languageTag, final String addressId);

    String CHANGE_ADDRESS_PROCESS = "changeAddressProcess";

    Call changeAddressProcessCall(final String languageTag, final String addressId);

    String REMOVE_ADDRESS_PROCESS = "removeAddressProcess";

    Call removeAddressProcessCall(final String languageTag, final String addressId);
}
