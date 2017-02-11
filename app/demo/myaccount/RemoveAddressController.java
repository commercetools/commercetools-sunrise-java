package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.SunriseRemoveAddressController;

import javax.inject.Inject;

public class RemoveAddressController extends SunriseRemoveAddressController {

    @Inject
    public RemoveAddressController(final AddressBookReverseRouter addressBookReverseRouter,
                                   final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(addressBookReverseRouter, addressBookPageContentFactory);
    }
}
