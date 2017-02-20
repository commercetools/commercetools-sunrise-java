package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.mydetails.DefaultMyPersonalDetailsFormData;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsControllerAction;
import com.commercetools.sunrise.myaccount.mydetails.SunriseMyPersonalDetailsController;
import com.commercetools.sunrise.myaccount.mydetails.viewmodels.MyPersonalDetailsPageContentFactory;
import com.commercetools.sunrise.sessions.customer.CustomerOperationsControllerComponentSupplier;
import controllers.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        CustomerOperationsControllerComponentSupplier.class
})
public final class MyPersonalDetailsController extends SunriseMyPersonalDetailsController<DefaultMyPersonalDetailsFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;
    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyPersonalDetailsController(final TemplateRenderer templateRenderer,
                                       final FormFactory formFactory,
                                       final CustomerFinder customerFinder,
                                       final MyPersonalDetailsControllerAction myPersonalDetailsControllerAction,
                                       final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory,
                                       final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter,
                                       final AuthenticationReverseRouter authenticationReverseRouter) {
        super(templateRenderer, formFactory, customerFinder, myPersonalDetailsControllerAction, myPersonalDetailsPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-personal-details";
    }

    @Override
    public Class<DefaultMyPersonalDetailsFormData> getFormDataClass() {
        return DefaultMyPersonalDetailsFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final DefaultMyPersonalDetailsFormData formData) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.logInPageCall());
    }
}
