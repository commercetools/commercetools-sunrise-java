package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithCmsPage;
import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * Controller for the home page.
 <p>Components that may be a fit</p>
 * <ul>
 *     <li>{@link ProductSuggestionsControllerComponent}</li>
 * </ul>
 * <p id="hooks">supported hooks</p>
 * <ul>
 *     <li>{@link RequestStartedHook}</li>
 *     <li>{@link PageDataReadyHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>home</li>
 *     <li>product-catalog</li>
 * </ul>
 */
public abstract class SunriseHomeController extends SunriseFrameworkController implements WithTemplateName, WithCmsPage, WithFetchFlow<Void> {

    private final HomePageContentFactory homePageContentFactory;

    protected SunriseHomeController(final HomePageContentFactory homePageContentFactory) {
        this.homePageContentFactory = homePageContentFactory;
    }

    @Override
    public String getTemplateName() {
        return "home";
    }

    @Override
    public String getCmsPageKey() {
        return "home";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("home", "product-catalog"));
    }

    @SunriseRoute("homePageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> showPage(null));
    }

    @Override
    public CompletionStage<Content> renderPage(final Void output) {
        return cmsPage().thenComposeAsync(cmsPage ->
                        renderPageWithTemplate(homePageContentFactory.create(null), getTemplateName(), cmsPage.orElse(null)),
                HttpExecution.defaultContext());
    }
}
