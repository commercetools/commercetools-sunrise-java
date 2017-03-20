package com.commercetools.sunrise.myaccount.addressbook.addaddress;

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
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.viewmodels.AddAddressPageContentFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddAddressController extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<Customer, Customer, AddressFormData>, WithRequiredCustomer {

    private final AddressFormData formData;
    private final CustomerFinder customerFinder;
    private final AddAddressControllerAction controllerAction;
    private final AddAddressPageContentFactory pageContentFactory;
    private final CountryCode country;

    protected SunriseAddAddressController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                          final AddressFormData formData, final CustomerFinder customerFinder,
                                          final AddAddressControllerAction controllerAction,
                                          final AddAddressPageContentFactory pageContentFactory,
                                          final CountryCode country) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.customerFinder = customerFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
        this.country = country;
    }

    @Override
    public final Class<? extends AddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(customer -> showFormPage(customer, formData));
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final AddressFormData formData) {
        return controllerAction.apply(customer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final AddressFormData formData);

    @Override
    public PageContent createPageContent(final Customer customer, final Form<? extends AddressFormData> form) {
        return pageContentFactory.create(customer, form);
    }

    @Override
    public void preFillFormData(final Customer customer, final AddressFormData formData) {
        final Address address = Address.of(country)
                .withTitle(customer.getTitle())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .withEmail(customer.getEmail());
        formData.applyAddress(address);
    }
}
