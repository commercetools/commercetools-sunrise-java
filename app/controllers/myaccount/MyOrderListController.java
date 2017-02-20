package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.MyOrderListFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels.MyOrderListPageContentFactory;
import com.commercetools.sunrise.sessions.customer.CustomerOperationsControllerComponentSupplier;
import controllers.PageHeaderControllerComponentSupplier;
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
public final class MyOrderListController extends SunriseMyOrderListController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyOrderListController(final TemplateRenderer templateRenderer,
                                 final FormFactory formFactory,
                                 final CustomerFinder customerFinder,
                                 final MyOrderListFinder myOrderListFinder,
                                 final MyOrderListPageContentFactory myOrderListPageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(templateRenderer, formFactory, customerFinder, myOrderListFinder, myOrderListPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-my-orders";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.logInPageCall());
    }
}
