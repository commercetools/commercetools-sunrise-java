package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.myaccount.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.myaccount.authentication.login.DefaultLogInFormData;
import com.commercetools.sunrise.myaccount.authentication.login.LogInExecutor;
import com.commercetools.sunrise.myaccount.authentication.login.SunriseLogInController;
import com.commercetools.sunrise.myaccount.authentication.login.view.LogInPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
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
