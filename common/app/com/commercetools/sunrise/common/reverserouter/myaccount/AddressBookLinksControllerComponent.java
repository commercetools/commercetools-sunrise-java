package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class AddressBookLinksControllerComponent extends AbstractLinksControllerComponent<AddressBookReverseRouter> {

    private final AddressBookReverseRouter reverseRouter;

    @Inject
    protected AddressBookLinksControllerComponent(final AddressBookReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final AddressBookReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final AddressBookReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.addAddressToAddressBookCall(), "myAddressBookAddAddress");
        meta.addHalLink(reverseRouter.addAddressToAddressBookProcessFormCall(), "myAddressBookAddAddressSubmit");
        meta.addHalLink(reverseRouter.addressBookCall(), "myAddressBook");
    }
}
