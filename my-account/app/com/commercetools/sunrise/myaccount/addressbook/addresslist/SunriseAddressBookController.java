package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookController extends SunriseTemplateController implements WithQueryFlow<Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseAddressBookController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                           final CustomerFinder customerFinder,
                                           final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(componentRegistry, templateRenderer);
        this.customerFinder = customerFinder;
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @Inject
    private void registerThemeLinks(final AddressBookThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @SunriseRoute("addressBookCall")
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(this::showPage);
    }

    @Override
    public PageContent createPageContent(final Customer customer) {
        return addressBookPageContentFactory.create(customer);
    }
}