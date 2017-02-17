package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.authentication.signup.DefaultSignUpFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpActionExecutor;
import com.commercetools.sunrise.myaccount.authentication.signup.SunriseSignUpController;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpPageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class SignUpController extends SunriseSignUpController<DefaultSignUpFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public SignUpController(final ComponentRegistry componentRegistry,
                            final TemplateRenderer templateRenderer,
                            final FormFactory formFactory,
                            final SignUpActionExecutor signUpActionExecutor,
                            final SignUpPageContentFactory signUpPageContentFactory,
                            final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(componentRegistry, templateRenderer, formFactory, signUpActionExecutor, signUpPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
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
