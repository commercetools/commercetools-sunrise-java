package controllers.myaccount;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordControllerAction;
import com.commercetools.sunrise.myaccount.authentication.changepassword.ChangePasswordFormData;
import com.commercetools.sunrise.myaccount.authentication.changepassword.SunriseChangePasswordController;
import com.commercetools.sunrise.myaccount.authentication.changepassword.viemodels.ChangePasswordPageContentFactory;
import io.sphere.sdk.customers.Customer;
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
public final class ChangePasswordController extends SunriseChangePasswordController {

    private final AuthenticationReverseRouter authenticationReverseRouter;
    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;

    @Inject
    public ChangePasswordController(final ContentRenderer contentRenderer,
                                    final FormFactory formFactory,
                                    final ChangePasswordFormData formData,
                                    final CustomerFinder customerFinder,
                                    final ChangePasswordControllerAction controllerAction,
                                    final ChangePasswordPageContentFactory pageContentFactory,
                                    final AuthenticationReverseRouter authenticationReverseRouter,
                                    final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter) {
        super(contentRenderer, formFactory, formData, customerFinder, controllerAction, pageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-change-password";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectToCall(authenticationReverseRouter.logInPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer result, final ChangePasswordFormData formData) {
        return redirectToCall(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }
}
