package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AddAddressController extends SunriseAddAddressController {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public AddAddressController(final CountryCode country, final CustomerFinder customerFinder,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final AuthenticationReverseRouter authenticationReverseRouter,
                                final AddressBookReverseRouter addressBookReverseRouter) {
        super(country, customerFinder, addAddressPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final AddressBookAddressFormData formData, final Customer oldCustomer, final Customer updatedCustomer) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundAddress() {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
