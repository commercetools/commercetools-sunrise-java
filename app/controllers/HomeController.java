package controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.PageContent;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller for the home page.
 */
@Singleton
public class HomeController extends SunriseController {

    @Inject
    public HomeController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final PageContent pageContent = new HomeContent();
        return F.Promise.pure(ok(templateService().renderToHtml("home", pageData(userContext, pageContent), userContext.locales())));
    }

    private static class HomeContent extends PageContent {
        public HomeContent() {

        }

        @Override
        public String getAdditionalTitle() {
            return null;
        }
    }
}
