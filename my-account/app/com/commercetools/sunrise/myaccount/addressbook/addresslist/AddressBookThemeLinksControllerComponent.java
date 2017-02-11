package com.commercetools.sunrise.myaccount.addressbook.addresslist;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;

import javax.inject.Inject;

final class AddressBookThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AddressBookReverseRouter reverseRouter;

    @Inject
    AddressBookThemeLinksControllerComponent(final AddressBookReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.addressBookCall(), "myAddressBook");
    }
}