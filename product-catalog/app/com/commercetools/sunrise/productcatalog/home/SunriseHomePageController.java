package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Controller for the home page.
 <p>Components that may be a fit</p>
 * <ul>
 *     <li>{@link ProductSuggestionsControllerComponent}</li>
 * </ul>
 * <p id="hooks">supported hooks</p>
 * <ul>
 *     <li>{@link RequestHook}</li>
 *     <li>{@link SunrisePageDataHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>home</li>
 *     <li>product-catalog</li>
 * </ul>
 */
@RequestScoped
public abstract class SunriseHomePageController extends SunriseFrameworkController implements WithOverwriteableTemplateName {

    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private HomeReverseRouter homeReverseRouter;

    @Override
    public String getTemplateName() {
        return "home";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("home", "product-catalog"));
    }

    public Result index() {
        return redirect(homeReverseRouter.homePageCall(userContext().locale().toLanguageTag()));
    }

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(this::showHome);
    }

    protected CompletableFuture<Result> showHome() {
        final PageContent pageContent = createPageContent();
        return completedFuture(ok(renderPage(pageContent, getTemplateName())));
    }

    protected PageContent createPageContent() {
        final HomePageContent pageContent = new HomePageContent();
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext().locales(), I18nIdentifier.of("catalog:home.title")));
        return pageContent;
    }
}
