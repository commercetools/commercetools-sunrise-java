package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.SunriseAddAddressController;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class AddAddressController extends SunriseAddAddressController {

    @Inject
    public AddAddressController(final ContentRenderer contentRenderer,
                                final AddAddressControllerAction controllerAction) {
        super(contentRenderer, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public Result handleSuccessfulAction() {
        return redirect(routes.AddressBookDetailController.show());
    }
}
