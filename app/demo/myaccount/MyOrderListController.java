package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.MyOrderListFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
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
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
