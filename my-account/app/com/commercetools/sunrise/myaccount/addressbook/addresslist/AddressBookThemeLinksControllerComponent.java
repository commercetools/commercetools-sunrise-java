package com.commercetools.sunrise.myaccount.addressbook.addresslist;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AddressBookLocalizedReverseRouter;

import javax.inject.Inject;

final class AddressBookThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AddressBookLocalizedReverseRouter reverseRouter;

    @Inject
    AddressBookThemeLinksControllerComponent(final AddressBookLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.addressBookCall(), "myAddressBook");
    }
}