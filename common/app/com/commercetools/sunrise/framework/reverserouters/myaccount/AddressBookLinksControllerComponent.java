package com.commercetools.sunrise.framework.reverserouters.myaccount;

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
        meta.addHalLink(reverseRouter.addAddressPageCall(), "myAddressBookAddAddress");
        meta.addHalLink(reverseRouter.addAddressProcessCall(), "myAddressBookAddAddressSubmit");
        meta.addHalLink(reverseRouter.addressBookDetailPageCall(), "myAddressBook");
    }
}
