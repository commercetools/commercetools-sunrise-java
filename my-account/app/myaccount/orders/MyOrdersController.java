package myaccount.orders;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class MyOrdersController extends SunriseController {

    @Inject
    public MyOrdersController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public Result show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final MyOrdersPageContent pageContent = new MyOrdersPageContent();
        return ok(renderMyOrdersPage(pageContent, userContext));
    }

    private Html renderMyOrdersPage(final MyOrdersPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("my-account-my-orders", pageData, userContext.locales());
    }
}
