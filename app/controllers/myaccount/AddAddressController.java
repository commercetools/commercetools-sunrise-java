package controllers.myaccount;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.viewmodels.AddAddressPageContentFactory;
import com.commercetools.sunrise.models.customers.CustomerOperationsControllerComponentSupplier;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CustomerOperationsControllerComponentSupplier.class
})
public final class AddAddressController extends SunriseAddAddressController {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public AddAddressController(final ContentRenderer contentRenderer,
                                final FormFactory formFactory,
                                final AddressFormData formData,
                                final CustomerFinder customerFinder,
                                final AddAddressControllerAction controllerAction,
                                final AddAddressPageContentFactory pageContentFactory,
                                final CountryCode country,
                                final AuthenticationReverseRouter authenticationReverseRouter,
                                final AddressBookReverseRouter addressBookReverseRouter) {
        super(contentRenderer, formFactory, formData, customerFinder, controllerAction, pageContentFactory, country);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final AddressFormData formData) {
        return redirectToCall(addressBookReverseRouter.addressBookDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectToCall(authenticationReverseRouter.logInPageCall());
    }
}
