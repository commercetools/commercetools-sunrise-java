package controllers.myaccount;

import com.commercetools.sunrise.common.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.login.DefaultLogInFormData;
import com.commercetools.sunrise.myaccount.authentication.login.LogInExecutor;
import com.commercetools.sunrise.myaccount.authentication.login.SunriseLogInController;
import com.commercetools.sunrise.myaccount.authentication.login.view.LogInPageContentFactory;
import controllers.PageHeaderControllerComponentsSupplier;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class LogInController extends SunriseLogInController<DefaultLogInFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public LogInController(final TemplateRenderer templateRenderer,
                           final FormFactory formFactory,
                           final LogInExecutor logInExecutor,
                           final LogInPageContentFactory logInPageContentFactory,
                           final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(templateRenderer, formFactory, logInExecutor, logInPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    @Override
    public Class<DefaultLogInFormData> getFormDataClass() {
        return DefaultLogInFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final DefaultLogInFormData formData) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
