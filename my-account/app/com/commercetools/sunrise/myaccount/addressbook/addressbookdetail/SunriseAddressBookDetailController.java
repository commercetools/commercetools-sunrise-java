package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail;


import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookDetailController extends SunriseContentController
        implements MyAccountController, WithQueryFlow<Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseAddressBookDetailController(final ContentRenderer contentRenderer, final CustomerFinder customerFinder,
                                                 final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(contentRenderer);
        this.customerFinder = customerFinder;
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADDRESS_BOOK_DETAIL_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(this::showPage);
    }

    @Override
    public PageContent createPageContent(final Customer customer) {
        return addressBookPageContentFactory.create(customer);
    }
}