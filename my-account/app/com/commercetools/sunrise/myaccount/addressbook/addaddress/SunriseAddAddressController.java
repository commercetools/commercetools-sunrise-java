package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContentFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddAddressController<F extends AddressBookAddressFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Customer, Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final AddAddressExecutor addAddressExecutor;
    private final AddAddressPageContentFactory addAddressPageContentFactory;
    private final CountryCode country;

    protected SunriseAddAddressController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                          final FormFactory formFactory, final CustomerFinder customerFinder,
                                          final AddAddressExecutor addAddressExecutor,
                                          final AddAddressPageContentFactory addAddressPageContentFactory,
                                          final CountryCode country) {
        super(componentRegistry, templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.addAddressExecutor = addAddressExecutor;
        this.addAddressPageContentFactory = addAddressPageContentFactory;
        this.country = country;
    }

    @Inject
    private void registerThemeLinks(final AddAddressThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @SunriseRoute("addAddressToAddressBookCall")
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(this::showFormPage);
    }

    @SunriseRoute("addAddressToAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final F formData) {
        return addAddressExecutor.apply(customer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Customer customer, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(customer, form);
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
