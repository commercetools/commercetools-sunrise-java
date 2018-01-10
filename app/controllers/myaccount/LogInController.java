package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.authentication.login.LogInControllerAction;
import com.commercetools.sunrise.myaccount.authentication.login.LogInFormData;
import com.commercetools.sunrise.myaccount.authentication.login.SunriseLogInController;
import com.commercetools.sunrise.myaccount.authentication.login.viewmodels.LogInPageContentFactory;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class LogInController extends SunriseLogInController {

    @Inject
    public LogInController(final ContentRenderer contentRenderer,
                           final FormFactory formFactory,
                           final LogInFormData formData,
                           final LogInControllerAction controllerAction,
                           final LogInPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory, formData, controllerAction, pageContentFactory);
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
        return redirectAsync(routes.MyPersonalDetailsController.show());
    }
}
