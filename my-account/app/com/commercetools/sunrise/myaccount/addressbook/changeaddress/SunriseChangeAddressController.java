package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeAddressController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<String, Customer, AddressFormData> {

    private final AddressFormData formData;
    private final ChangeAddressControllerAction controllerAction;

    protected SunriseChangeAddressController(final ContentRenderer contentRenderer,
                                             final FormFactory formFactory,
                                             final AddressFormData formData,
                                             final ChangeAddressControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String addressIdentifier) {
        return showFormPage(addressIdentifier, formData);
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String addressIdentifier) {
        return processForm(addressIdentifier);
    }

    @Override
    public CompletionStage<Customer> executeAction(final String addressIdentifier, final AddressFormData formData) {
        return controllerAction.apply(addressIdentifier, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final AddressFormData formData);

    // TODO move this to templates
    @Override
    public void preFillFormData(final String addressId, final AddressFormData formData) {
//        final Address address = addressWithCustomer.getAddress();
//        final Customer customer = addressWithCustomer.getCustomer();
//        formData.applyAddress(address);
//        formData.applyDefaultShippingAddress(isDefaultAddress(address, customer.getDefaultShippingAddressId()));
//        formData.applyDefaultBillingAddress(isDefaultAddress(address, customer.getDefaultBillingAddressId()));
    }

    @Override
    public PageContent createPageContent(final String addressIdentifier, final Form<? extends AddressFormData> form) {
        return new BlankPageContent();
    }

    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}