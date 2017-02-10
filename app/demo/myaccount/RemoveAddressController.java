package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.SunriseRemoveAddressController;

import javax.inject.Inject;

public class RemoveAddressController extends SunriseRemoveAddressController {

    @Inject
    public RemoveAddressController(final AddressBookLocalizedReverseRouter addressBookReverseRouter,
                                   final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(addressBookReverseRouter, addressBookPageContentFactory);
    }
}
