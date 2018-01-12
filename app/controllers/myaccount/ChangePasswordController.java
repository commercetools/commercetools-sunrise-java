package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.changepassword.SunriseChangePasswordController;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class ChangePasswordController extends SunriseChangePasswordController {

    @Inject
    public ChangePasswordController(final ContentRenderer contentRenderer,
                                    final ChangePasswordControllerAction controllerAction) {
        super(contentRenderer, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-change-password";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public Result handleSuccessfulAction() {
        return redirect(routes.MyPersonalDetailsController.show());
    }
}
