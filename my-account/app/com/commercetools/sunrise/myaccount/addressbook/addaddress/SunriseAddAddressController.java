package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddAddressController extends SunriseContentFormController implements WithContentFormFlow<Void, Customer, AddressFormData> {

    private final AddressFormData formData;
    private final AddAddressControllerAction controllerAction;

    protected SunriseAddAddressController(final ContentRenderer contentRenderer,
                                          final FormFactory formFactory,
                                          final AddressFormData formData,
                                          final AddAddressControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PAGE)
    public CompletionStage<Result> show() {
        return okResultWithPageContent()
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Void input, final AddressFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer customer, final AddressFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends AddressFormData> form) {
        return new BlankPageContent();
    }

    // TODO move this to template
    @Override
    public void preFillFormData(final Void input, final AddressFormData formData) {
//        final Address address = Address.of(country)
//                .withTitle(customer.getTitle())
//                .withFirstName(customer.getFirstName())
//                .withLastName(customer.getLastName())
//                .withEmail(customer.getEmail());
//        formData.applyAddress(address);
    }
}
