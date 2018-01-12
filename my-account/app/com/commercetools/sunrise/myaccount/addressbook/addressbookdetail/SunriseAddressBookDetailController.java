package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail;


import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookDetailController extends SunriseContentController implements WithContent {

    protected SunriseAddressBookDetailController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADDRESS_BOOK_DETAIL_PAGE)
    public CompletionStage<Result> show() {
        return okResult(PageData.of());
    }
}