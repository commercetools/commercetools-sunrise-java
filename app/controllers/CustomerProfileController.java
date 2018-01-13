package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.myaccount.customerprofile.CustomerProfileFormAction;
import com.commercetools.sunrise.myaccount.customerprofile.SunriseCustomerProfileController;
import controllers.myaccount.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class CustomerProfileController extends SunriseCustomerProfileController {

    @Inject
    CustomerProfileController(final TemplateEngine templateEngine,
                              final CustomerProfileFormAction formAction) {
        super(templateEngine, formAction);
    }

    @Override
    protected Result onCustomerProfileEdited() {
        return redirect(routes.MyPersonalDetailsController.show());
    }
}
