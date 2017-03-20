package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.WithRequiredAddress;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveAddressController extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<AddressWithCustomer, Customer, RemoveAddressFormData>, WithRequiredCustomer, WithRequiredAddress {

    private final RemoveAddressFormData formData;
    private final CustomerFinder customerFinder;
    private final AddressFinder addressFinder;
    private final RemoveAddressControllerAction controllerAction;
    private final AddressBookPageContentFactory pageContentFactory;

    protected SunriseRemoveAddressController(final TemplateRenderer templateRenderer,
                                             final FormFactory formFactory, final RemoveAddressFormData formData,
                                             final CustomerFinder customerFinder, final AddressFinder addressFinder,
                                             final RemoveAddressControllerAction controllerAction,
                                             final AddressBookPageContentFactory pageContentFactory) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.customerFinder = customerFinder;
        this.addressFinder = addressFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends RemoveAddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public final AddressFinder getAddressFinder() {
        return addressFinder;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.REMOVE_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag, final String addressIdentifier) {
        return requireCustomer(customer ->
                requireAddress(customer, addressIdentifier,
                        address -> processForm(AddressWithCustomer.of(address, customer))));
    }

    @Override
    public CompletionStage<Customer> executeAction(final AddressWithCustomer addressWithCustomer, final RemoveAddressFormData formData) {
        return controllerAction.apply(addressWithCustomer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final RemoveAddressFormData formData);

    @Override
    public void preFillFormData(final AddressWithCustomer input, final RemoveAddressFormData formData) {
        // Do not pre-fill anything
    }

    @Override
    public PageContent createPageContent(final AddressWithCustomer addressWithCustomer, final Form<? extends RemoveAddressFormData> form) {
        return pageContentFactory.create(addressWithCustomer.getCustomer());
    }
}
