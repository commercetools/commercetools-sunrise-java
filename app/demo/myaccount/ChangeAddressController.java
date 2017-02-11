package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.ChangeAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.SunriseChangeAddressController;

import javax.inject.Inject;

public class ChangeAddressController extends SunriseChangeAddressController {

    @Inject
    public ChangeAddressController(final AddressBookReverseRouter addressBookReverseRouter,
                                   final ChangeAddressPageContentFactory changeAddressPageContentFactory) {
        super(addressBookReverseRouter, changeAddressPageContentFactory);
    }
}
