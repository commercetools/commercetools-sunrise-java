package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookLocalizedReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import com.neovisionaries.i18n.CountryCode;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AddAddressController extends SunriseAddAddressController {

    private final AuthenticationLocalizedReverseRouter authenticationReverseRouter;

    @Inject
    public AddAddressController(final AddressBookLocalizedReverseRouter addressBookReverseRouter, final CountryCode country,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final AuthenticationLocalizedReverseRouter authenticationReverseRouter) {
        super(addressBookReverseRouter, country, addAddressPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }

            return redirectTo(addressBookReverseRouter.addressBookCall());

}
