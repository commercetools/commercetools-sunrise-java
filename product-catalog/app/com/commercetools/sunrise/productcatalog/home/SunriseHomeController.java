package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * Controller for the home page.
 */
public abstract class SunriseHomeController extends SunriseTemplateController implements WithQueryFlow<Void> {

    private final HomePageContentFactory homePageContentFactory;

    protected SunriseHomeController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                    final HomePageContentFactory homePageContentFactory) {
        super(hookContext, templateRenderer);
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
    public PageContent createPageContent(final Void input) {
        return homePageContentFactory.create(null);
    }
}
