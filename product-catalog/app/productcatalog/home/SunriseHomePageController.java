package productcatalog.home;

import common.controllers.SunriseFrameworkController;
import common.controllers.SunrisePageData;
import common.hooks.RequestHook;
import common.hooks.SunrisePageDataHook;
import common.inject.RequestScoped;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Controller for the home page.
 */
@RequestScoped
public abstract class SunriseHomePageController extends SunriseFrameworkController {

    @Inject
    private I18nResolver i18nResolver;

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> completedFuture(ok(renderHomePage(new HomePageContent()))));
    }

    protected Html renderHomePage(final HomePageContent pageContent) {
        pageContent.setAdditionalTitle(i18nResolver.getOrEmpty(userContext().locales(), I18nIdentifier.of("catalog:home.title")));
        final SunrisePageData pageData = pageData(userContext(), pageContent, ctx(), session());
        runVoidHook(SunrisePageDataHook.class, hook -> hook.acceptSunrisePageData(pageData));
        return templateEngine().renderToHtml("home", pageData, userContext().locales());
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("home"));
    }
}
