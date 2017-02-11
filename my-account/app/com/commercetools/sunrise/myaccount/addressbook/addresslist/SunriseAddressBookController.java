package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(AddressBookThemeLinksControllerComponent.class)
public abstract class SunriseAddressBookController extends SunriseFrameworkMyAccountController implements WithTemplateName {

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
        return doRequest(() -> requireCustomer(this::showAddressBook));
    }

    protected CompletionStage<Result> showAddressBook(final Customer customer) {
        return asyncOk(renderPage(customer));
    }

    protected CompletionStage<Html> renderPage(final Customer customer) {
        final AddressBookControllerData addressBookControllerData = new AddressBookControllerData(customer, null);
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(addressBookControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}