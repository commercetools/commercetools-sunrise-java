package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.SunriseAddressBookController;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AddressBookController extends SunriseAddressBookController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public AddressBookController(final CustomerFinder customerFinder,
                                 final AddressBookPageContentFactory addressBookPageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(customerFinder, addressBookPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
