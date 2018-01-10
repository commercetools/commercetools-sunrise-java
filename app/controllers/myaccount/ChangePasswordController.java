package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.changepassword.SunriseChangePasswordController;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class ChangePasswordController extends SunriseChangePasswordController {

    @Inject
    public ChangePasswordController(final ContentRenderer contentRenderer,
                                    final FormFactory formFactory,
                                    final ChangePasswordFormData formData,
                                    final ChangePasswordControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
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
    public CompletionStage<Result> handleSuccessfulAction(final Customer result, final ChangePasswordFormData formData) {
        return redirectAsync(routes.MyPersonalDetailsController.show());
    }
}
