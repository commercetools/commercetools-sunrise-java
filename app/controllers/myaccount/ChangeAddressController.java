package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.addresses.MyAddressFetcher;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.ChangeAddressControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.SunriseChangeAddressController;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class ChangeAddressController extends SunriseChangeAddressController {

    @Inject
    public ChangeAddressController(final ContentRenderer contentRenderer,
                                   final ChangeAddressControllerAction controllerAction,
                                   final MyAddressFetcher myAddressFetcher) {
        super(contentRenderer, controllerAction, myAddressFetcher);
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
    public Result handleSuccessfulAction() {
        return redirect(routes.AddressBookDetailController.show());
    }
}
