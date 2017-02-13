package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.mydetails.DefaultMyPersonalDetailsFormData;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsExecutor;
import com.commercetools.sunrise.myaccount.mydetails.SunriseMyPersonalDetailsController;
import com.commercetools.sunrise.myaccount.mydetails.view.MyPersonalDetailsPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class MyPersonalDetailsController extends SunriseMyPersonalDetailsController<DefaultMyPersonalDetailsFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;
    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyPersonalDetailsController(final TemplateRenderer templateRenderer,
                                       final RequestHookContext hookContext,
                                       final FormFactory formFactory,
                                       final CustomerFinder customerFinder,
                                       final MyPersonalDetailsExecutor myPersonalDetailsExecutor,
                                       final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory,
                                       final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter,
                                       final AuthenticationReverseRouter authenticationReverseRouter) {
        super(templateRenderer, hookContext, formFactory, customerFinder, myPersonalDetailsExecutor, myPersonalDetailsPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
        this.authenticationReverseRouter = authenticationReverseRouter;
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
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
