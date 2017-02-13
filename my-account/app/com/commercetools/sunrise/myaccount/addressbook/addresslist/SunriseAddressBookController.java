package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContent;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(AddressBookThemeLinksControllerComponent.class)
public abstract class SunriseAddressBookController extends SunriseFrameworkMyAccountController implements WithTemplateName, WithQueryFlow<Customer> {

    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseAddressBookController(final CustomerFinder customerFinder, final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(customerFinder);
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @SunriseRoute("addressBookCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireCustomer(this::showPage));
    }

    @Override
    public CompletionStage<Html> createPageContent(final Customer customer) {
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(customer);
        return renderContent(pageContent, getTemplateName());
    }
}