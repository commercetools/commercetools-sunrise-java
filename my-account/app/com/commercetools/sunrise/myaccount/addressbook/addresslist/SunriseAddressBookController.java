package com.commercetools.sunrise.myaccount.addressbook.addresslist;


import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@RequestScoped
@IntroducingMultiControllerComponents(SunriseAddressBookHeroldComponent.class)
public abstract class SunriseAddressBookController extends SunriseFrameworkMyAccountController implements WithTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseAddressBookController.class);

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

    @SunriseRoute("addressBookCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            logger.debug("show address book in locale={}", languageTag);
            return requireExistingCustomer()
                    .thenComposeAsync(this::showAddressBook, HttpExecution.defaultContext());
        });
    }

    protected CompletionStage<Result> showAddressBook(final Customer customer) {
        return asyncOk(renderPage(customer));
    }

    protected CompletionStage<Html> renderPage(final Customer customer) {
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(customer);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}