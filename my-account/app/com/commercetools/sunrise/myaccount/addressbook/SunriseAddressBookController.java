package com.commercetools.sunrise.myaccount.addressbook;


import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.common.MyAccountController;
import io.sphere.sdk.customers.Customer;
import play.filters.csrf.AddCSRFToken;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseAddressBookController extends MyAccountController implements WithOverwriteableTemplateName {

    @Inject
    private AddressBookPageContentFactory addressBookPageContentFactory;

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

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> getCustomerAndExecute(this::showAddressBook));
    }

    protected CompletionStage<Result> showAddressBook(@Nullable final Customer customer) {
        final PageContent pageContent = createPageContent(customer);
        return completedFuture(ok(renderPage(pageContent, getTemplateName())));
    }

    protected PageContent createPageContent(final Customer customer) {
        return addressBookPageContentFactory.create(customer);
    }
}