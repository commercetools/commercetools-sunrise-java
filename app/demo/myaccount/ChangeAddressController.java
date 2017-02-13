package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.ChangeAddressExecutor;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.SunriseChangeAddressController;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class ChangeAddressController extends SunriseChangeAddressController<DefaultAddressBookAddressFormData> {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public ChangeAddressController(final TemplateRenderer templateRenderer,
                                   final RequestHookContext hookContext,
                                   final FormFactory formFactory,
                                   final CustomerFinder customerFinder,
                                   final AddressFinder addressFinder,
                                   final ChangeAddressExecutor changeAddressExecutor,
                                   final ChangeAddressPageContentFactory changeAddressPageContentFactory,
                                   final AuthenticationReverseRouter authenticationReverseRouter,
                                   final AddressBookReverseRouter addressBookReverseRouter) {
        super(templateRenderer, hookContext, formFactory, customerFinder, addressFinder, changeAddressExecutor, changeAddressPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public Class<DefaultAddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }

    @Override
    public CompletionStage<Result> handleNotFoundAddress() {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final DefaultAddressBookAddressFormData formData) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }
}
