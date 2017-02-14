package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(AddressBookThemeLinksControllerComponent.class)
public abstract class SunriseAddressBookController extends SunriseTemplateController implements WithQueryFlow<Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseAddressBookController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                           final CustomerFinder customerFinder,
                                           final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(hookContext, templateRenderer);
        this.customerFinder = customerFinder;
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("address-book"));
        return frameworkTags;
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
        return doRequest(() -> requireCustomer(this::showPage));
    }

    @Override
    public PageContent createPageContent(final Customer customer) {
        return addressBookPageContentFactory.create(customer);
    }
}