package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.SunriseResetPasswordController;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class ResetPasswordController extends SunriseResetPasswordController {

    @Inject
    ResetPasswordController(final ContentRenderer contentRenderer,
                            final FormFactory formFactory,
                            final ResetPasswordFormData formData,
                            final ResetPasswordControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-reset-password";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer customer, final ResetPasswordFormData formData) {
        return redirectAsync(routes.LogInController.show());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundToken(final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        saveFormError(form, "Reset token is not valid");
        return showFormPageWithErrors(resetToken, form);
    }
}
