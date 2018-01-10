package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.ChangeAddressControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.SunriseChangeAddressController;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class ChangeAddressController extends SunriseChangeAddressController {

    @Inject
    public ChangeAddressController(final ContentRenderer contentRenderer,
                                   final FormFactory formFactory,
                                   final AddressFormData formData,
                                   final ChangeAddressControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-edit-address";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final AddressFormData formData) {
        return redirectAsync(routes.AddressBookDetailController.show());
    }
}
