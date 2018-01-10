package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.SunriseAddressBookDetailController;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class AddressBookDetailController extends SunriseAddressBookDetailController {

    @Inject
    public AddressBookDetailController(final ContentRenderer contentRenderer,
                                       final AddressBookPageContentFactory pageContentFactory) {
        super(contentRenderer, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
