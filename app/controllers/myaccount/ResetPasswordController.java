package controllers.myaccount;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.SunriseResetPasswordController;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.viewmodels.ResetPasswordPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class
})
public final class ResetPasswordController extends SunriseResetPasswordController {
    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    ResetPasswordController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                   final ResetPasswordFormData formData,
                                   final ResetPasswordControllerAction controllerAction,
                                   final ResetPasswordPageContentFactory pageContentFactory,
                                   final AuthenticationReverseRouter authenticationReverseRouter) {
        super(contentRenderer, formFactory, formData, controllerAction, pageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
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
        return redirectToCall(authenticationReverseRouter.logInPageCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundToken(final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        saveFormError(form, "Reset token is not valid");
        return showFormPageWithErrors(resetToken, form);
    }
}
