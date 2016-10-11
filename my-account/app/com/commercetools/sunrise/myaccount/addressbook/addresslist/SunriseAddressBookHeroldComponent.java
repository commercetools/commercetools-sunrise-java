package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;

import javax.inject.Inject;

final class SunriseAddressBookHeroldComponent extends HeroldComponentBase {
    @Inject
    private AddressBookReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.addressBookCall(languageTag()), "myAddressBook");
    }
}