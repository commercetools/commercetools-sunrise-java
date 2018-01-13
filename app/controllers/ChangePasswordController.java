package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.myaccount.changepassword.ChangePasswordFormAction;
import com.commercetools.sunrise.myaccount.changepassword.SunriseChangePasswordController;
import controllers.myaccount.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class ChangePasswordController extends SunriseChangePasswordController {

    @Inject
    public ChangePasswordController(final TemplateEngine templateEngine,
                                    final ChangePasswordFormAction controllerAction) {
        super(templateEngine, controllerAction);
    }

    @Override
    public Result onPasswordChanged() {
        return redirect(routes.ChangePasswordController.show());
    }
}
