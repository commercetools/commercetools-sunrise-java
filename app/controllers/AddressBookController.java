package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.addresses.MyAddressFetcher;
import com.commercetools.sunrise.myaccount.addressbook.RemoveAddressFormAction;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookController;
import com.commercetools.sunrise.myaccount.addressbook.AddAddressFormAction;
import com.commercetools.sunrise.myaccount.addressbook.ChangeAddressFormAction;
import controllers.myaccount.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class AddressBookController extends SunriseAddressBookController {

    @Inject
    AddressBookController(final TemplateEngine templateEngine,
                          final MyAddressFetcher myAddressFetcher,
                          final AddAddressFormAction addAddressFormAction,
                          final ChangeAddressFormAction changeAddressFormAction,
                          final RemoveAddressFormAction removeAddressFormAction) {
        super(templateEngine, myAddressFetcher, addAddressFormAction, changeAddressFormAction, removeAddressFormAction);
    }

    @Override
    protected Result onAddressRemoved() {
        return redirect(controllers.myaccount.routes.AddressBookController.show());
    }

    @Override
    protected Result onAddressAdded() {
        return redirect(controllers.myaccount.routes.AddressBookController.show());
    }

    @Override
    protected Result onAddressChanged() {
        return redirect(routes.AddressBookController.show());
    }
}
