package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail;


import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookDetailController extends SunriseContentController implements MyAccountController, WithQueryFlow<Void> {

    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseAddressBookDetailController(final ContentRenderer contentRenderer,
                                                 final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(contentRenderer);
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADDRESS_BOOK_DETAIL_PAGE)
    public CompletionStage<Result> show() {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return addressBookPageContentFactory.create(null);
    }
}