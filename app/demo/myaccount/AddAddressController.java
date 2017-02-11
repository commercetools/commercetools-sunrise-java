package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressFunction;
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
                                final AddAddressFunction addAddressFunction,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final CountryCode country,
                                final AuthenticationReverseRouter authenticationReverseRouter,
                                final AddressBookReverseRouter addressBookReverseRouter) {
        super(customerFinder, addAddressFunction, addAddressPageContentFactory, country);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public Class<DefaultAddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultAddressBookAddressFormData formData, final Customer oldCustomer, final Customer updatedCustomer) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
