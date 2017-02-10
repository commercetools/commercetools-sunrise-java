package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookLocalizedReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
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

    private final AuthenticationLocalizedReverseRouter authenticationReverseRouter;
    private final AddressBookLocalizedReverseRouter addressBookLocalizedReverseRouter;

    @Inject
    public AddAddressController(final CountryCode country, final CustomerFinder customerFinder,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final AuthenticationLocalizedReverseRouter authenticationReverseRouter,
                                final AddressBookLocalizedReverseRouter addressBookLocalizedReverseRouter) {
        super(country, customerFinder, addAddressPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookLocalizedReverseRouter = addressBookLocalizedReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final AddressBookAddressFormData formData, final Customer oldCustomer, final Customer updatedCustomer) {
        return redirectTo(addressBookLocalizedReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundAddress() {
        return redirectTo(addressBookLocalizedReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
