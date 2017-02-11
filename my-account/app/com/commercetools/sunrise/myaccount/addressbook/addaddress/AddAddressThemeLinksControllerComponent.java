package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;

import javax.inject.Inject;

final class AddAddressThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AddressBookReverseRouter reverseRouter;

    @Inject
    AddAddressThemeLinksControllerComponent(final AddressBookReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.addAddressToAddressBookCall(), "myAddressBookAddAddress");
        meta.addHalLink(reverseRouter.addAddressToAddressBookProcessFormCall(), "myAddressBookAddAddressSubmit");
    }
}
