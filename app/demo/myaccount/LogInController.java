package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.login.DefaultLogInFormData;
import com.commercetools.sunrise.myaccount.authentication.login.LogInExecutor;
import com.commercetools.sunrise.myaccount.authentication.login.view.LogInPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.login.SunriseLogInController;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class LogInController extends SunriseLogInController<DefaultLogInFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public LogInController(final LogInExecutor logInExecutor,
                           final LogInPageContentFactory logInPageContentFactory,
                           final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(logInExecutor, logInPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public Class<DefaultLogInFormData> getFormDataClass() {
        return DefaultLogInFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultLogInFormData formData, final Void input, final CustomerSignInResult result) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
