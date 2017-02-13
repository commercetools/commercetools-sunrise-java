package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.signup.DefaultSignUpFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpExecutor;
import com.commercetools.sunrise.myaccount.authentication.signup.SunriseSignUpController;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpPageContentFactory;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class SignUpController extends SunriseSignUpController<DefaultSignUpFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public SignUpController(final FormFactory formFactory,
                            final SignUpExecutor signUpExecutor,
                            final SignUpPageContentFactory signUpPageContentFactory,
                            final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(formFactory, signUpExecutor, signUpPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public Class<DefaultSignUpFormData> getFormDataClass() {
        return DefaultSignUpFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void input, final DefaultSignUpFormData formData, final CustomerSignInResult result) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
