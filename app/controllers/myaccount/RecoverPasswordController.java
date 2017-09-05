package controllers.myaccount;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.RecoverPasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.RecoverPasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.SunriseRecoverPasswordController;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordPageContentFactory;
import com.commercetools.sunrise.email.EmailDeliveryException;
import io.sphere.sdk.customers.CustomerToken;
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
public final class RecoverPasswordController extends SunriseRecoverPasswordController {
    private final RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    @Inject
    RecoverPasswordController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                              final RecoverPasswordPageContentFactory pageContentFactory,
                              final RecoverPasswordFormData formData,
                              final RecoverPasswordControllerAction controllerAction,
                              final RecoverPasswordReverseRouter recoverPasswordReverseRouter) {
        super(contentRenderer, formFactory, pageContentFactory, formData, controllerAction);
        this.recoverPasswordReverseRouter = recoverPasswordReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-forgot-password";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CustomerToken customerToken, final RecoverPasswordFormData formData) {
        flash("success", "A message with further instructions has been sent to your email address");
        return redirectToCall(recoverPasswordReverseRouter.requestRecoveryEmailPageCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundEmail(final Form<? extends RecoverPasswordFormData> form) {
        saveFormError(form, "Email not found");
        return showFormPageWithErrors(null, form);
    }

    @Override
    protected CompletionStage<Result> handleEmailDeliveryException(final Form<? extends RecoverPasswordFormData> form, final EmailDeliveryException emailDeliveryException) {
        saveFormError(form, "Email delivery error");
        return internalServerErrorResultWithPageContent(createPageContent(null, form));
    }
}
