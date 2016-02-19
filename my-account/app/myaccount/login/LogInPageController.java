package myaccount.login;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LogInPageController extends SunriseController {

    @Inject
    public LogInPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return F.Promise.pure(renderLogInPage(userContext));
    }

    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return F.Promise.pure(renderLogInPage(userContext));
    }

    private Result renderLogInPage(final UserContext userContext) {
        final LogInPageContent content = new LogInPageContent();
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return ok(templateService().renderToHtml("my-account-login", pageData, userContext.locales()));
    }
}
