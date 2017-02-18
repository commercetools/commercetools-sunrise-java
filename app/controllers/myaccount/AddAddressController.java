package controllers.myaccount;

import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressExecutor;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContentFactory;
import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.CommonControllerComponentsSupplier;
import controllers.PageHeaderControllerComponentsSupplier;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RegisteredComponents({
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class AddAddressController extends SunriseAddAddressController<DefaultAddressBookAddressFormData> {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public AddAddressController(final TemplateRenderer templateRenderer,
                                final FormFactory formFactory,
                                final CustomerFinder customerFinder,
                                final AddAddressExecutor addAddressExecutor,
                                final AddAddressPageContentFactory addAddressPageContentFactory,
                                final CountryCode country,
                                final AuthenticationReverseRouter authenticationReverseRouter,
                                final AddressBookReverseRouter addressBookReverseRouter) {
        super(templateRenderer, formFactory, customerFinder, addAddressExecutor, addAddressPageContentFactory, country);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @Override
    public Class<DefaultAddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final DefaultAddressBookAddressFormData formData) {
        return redirectTo(addressBookReverseRouter.addressBookPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.logInPageCall());
    }
}
