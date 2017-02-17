package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.DefaultRemoveAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.RemoveAddressExecutor;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.SunriseRemoveAddressController;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class RemoveAddressController extends SunriseRemoveAddressController<DefaultRemoveAddressFormData> {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public RemoveAddressController(final ComponentRegistry componentRegistry,
                                   final TemplateRenderer templateRenderer,
                                   final FormFactory formFactory,
                                   final CustomerFinder customerFinder,
                                   final AddressFinder addressFinder,
                                   final RemoveAddressExecutor removeAddressExecutor,
                                   final AddressBookPageContentFactory addressBookPageContentFactory,
                                   final AuthenticationReverseRouter authenticationReverseRouter,
                                   final AddressBookReverseRouter addressBookReverseRouter) {
        super(componentRegistry, templateRenderer, formFactory, customerFinder, addressFinder, removeAddressExecutor, addressBookPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
    }

    @Override
    public Class<DefaultRemoveAddressFormData> getFormDataClass() {
        return DefaultRemoveAddressFormData.class;
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
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final DefaultRemoveAddressFormData formData) {
        return redirectTo(addressBookReverseRouter.addressBookCall());
    }
}
