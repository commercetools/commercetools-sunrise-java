package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.RemoveAddressControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.RemoveAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.removeaddress.SunriseRemoveAddressController;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class RemoveAddressController extends SunriseRemoveAddressController {

    @Inject
    public RemoveAddressController(final ContentRenderer contentRenderer,
                                   final FormFactory formFactory,
                                   final RemoveAddressFormData formData,
                                   final RemoveAddressControllerAction controllerAction,
                                   final AddressBookPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory, formData, controllerAction, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final RemoveAddressFormData formData) {
        return redirectAsync(routes.AddressBookDetailController.show());
    }
}
