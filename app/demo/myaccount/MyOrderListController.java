package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.MyOrderListFinder;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.SunriseMyOrderListController;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.view.MyOrderListPageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class MyOrderListController extends SunriseMyOrderListController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyOrderListController(final ComponentRegistry componentRegistry,
                                 final TemplateRenderer templateRenderer,
                                 final FormFactory formFactory,
                                 final CustomerFinder customerFinder,
                                 final MyOrderListFinder myOrderListFinder,
                                 final MyOrderListPageContentFactory myOrderListPageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(componentRegistry, templateRenderer, formFactory, customerFinder, myOrderListFinder, myOrderListPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
