package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressExecutor;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AddAddressController extends SunriseAddAddressController<DefaultAddressBookAddressFormData> {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public AddAddressController(final CustomerFinder customerFinder,
                                final AddAddressExecutor addAddressExecutor,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final CountryCode country,
                                final AuthenticationReverseRouter authenticationReverseRouter,
                                final AddressBookReverseRouter addressBookReverseRouter) {
        super(customerFinder, addAddressExecutor, addAddressPageContentFactory, country);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public Class<DefaultAddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer oldCustomer, final DefaultAddressBookAddressFormData formData, final Customer updatedCustomer) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
