package controllers.myaccount;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.reverserouters.myaccount.mydetails.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.login.LogInControllerAction;
import com.commercetools.sunrise.myaccount.authentication.login.LogInFormData;
import com.commercetools.sunrise.myaccount.authentication.login.SunriseLogInController;
import com.commercetools.sunrise.myaccount.authentication.login.viewmodels.LogInPageContentFactory;
import com.commercetools.sunrise.models.carts.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.models.customers.CustomerOperationsControllerComponentSupplier;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CustomerOperationsControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class LogInController extends SunriseLogInController {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public LogInController(final ContentRenderer contentRenderer,
                           final FormFactory formFactory,
                           final LogInFormData formData,
                           final LogInControllerAction controllerAction,
                           final LogInPageContentFactory pageContentFactory,
                           final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(contentRenderer, formFactory, formData, controllerAction, pageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final LogInFormData formData) {
        return redirectToCall(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
