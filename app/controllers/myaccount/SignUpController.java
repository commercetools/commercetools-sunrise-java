package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.authentication.signup.DefaultSignUpFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpControllerAction;
import com.commercetools.sunrise.myaccount.authentication.signup.SunriseSignUpController;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.SignUpPageContentFactory;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.sessions.customer.CustomerOperationsControllerComponentSupplier;
import controllers.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CustomerOperationsControllerComponentSupplier.class,
        CartOperationsControllerComponentSupplier.class
})
public final class SignUpController extends SunriseSignUpController<DefaultSignUpFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public SignUpController(final TemplateRenderer templateRenderer,
                            final FormFactory formFactory,
                            final SignUpControllerAction signUpControllerAction,
                            final SignUpPageContentFactory signUpPageContentFactory,
                            final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(templateRenderer, formFactory, signUpControllerAction, signUpPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    @Override
    public Class<DefaultSignUpFormData> getFormDataClass() {
        return DefaultSignUpFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final DefaultSignUpFormData formData) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
