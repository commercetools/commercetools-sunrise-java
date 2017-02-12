package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.ChangeAddressExecutor;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.SunriseChangeAddressController;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ChangeAddressController extends SunriseChangeAddressController<DefaultAddressBookAddressFormData> {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public ChangeAddressController(final CustomerFinder customerFinder, final ChangeAddressExecutor changeAddressExecutor, final ChangeAddressPageContentFactory changeAddressPageContentFactory, final AuthenticationReverseRouter authenticationReverseRouter, final AddressBookReverseRouter addressBookReverseRouter) {
        super(customerFinder, changeAddressExecutor, changeAddressPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public Class<DefaultAddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundAddress() {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultAddressBookAddressFormData formData, final AddressWithCustomer addressWithCustomer, final Customer updatedCustomer) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }
}
