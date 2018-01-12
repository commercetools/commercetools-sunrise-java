package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordFormAction;
import com.commercetools.sunrise.myaccount.authentication.changepassword.SunriseChangePasswordController;
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
    public String getTemplateName() {
        return "my-account-change-password";
    }

    @Override
    public Result onPasswordChanged() {
        return redirect(routes.MyPersonalDetailsController.show());
    }
}
