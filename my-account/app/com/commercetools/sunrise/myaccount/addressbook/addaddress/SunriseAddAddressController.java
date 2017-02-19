package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContentFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddAddressController<F extends AddressBookAddressFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Customer, Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final AddAddressControllerAction addAddressControllerAction;
    private final AddAddressPageContentFactory addAddressPageContentFactory;
    private final CountryCode country;

    protected SunriseAddAddressController(final TemplateRenderer templateRenderer,
                                          final FormFactory formFactory, final CustomerFinder customerFinder,
                                          final AddAddressControllerAction addAddressControllerAction,
                                          final AddAddressPageContentFactory addAddressPageContentFactory,
                                          final CountryCode country) {
        super(templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.addAddressControllerAction = addAddressControllerAction;
        this.addAddressPageContentFactory = addAddressPageContentFactory;
        this.country = country;
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(this::showFormPage);
    }

    @RunRequestStartedHook
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final F formData) {
        return addAddressControllerAction.apply(customer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final F formData);

    @Override
    public PageContent createPageContent(final Customer customer, final Form<F> form) {
        return addAddressPageContentFactory.create(customer, form);
    }

    @Override
    public void preFillFormData(final Customer customer, final F formData) {
        final Address address = Address.of(country)
                .withTitle(customer.getTitle())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .withEmail(customer.getEmail());
        formData.applyAddress(address);
    }
}
