package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.CustomerCreator;
import com.commercetools.sunrise.myaccount.authentication.signup.DefaultSignUpFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.SunriseSignUpController;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class SignUpController extends SunriseSignUpController<DefaultSignUpFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public SignUpController(final CustomerCreator customerCreator,
                            final AuthenticationPageContentFactory authenticationPageContentFactory,
                            final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(customerCreator, authenticationPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public Class<DefaultSignUpFormData> getFormDataClass() {
        return DefaultSignUpFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultSignUpFormData formData, final Void context, final CustomerSignInResult result) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
