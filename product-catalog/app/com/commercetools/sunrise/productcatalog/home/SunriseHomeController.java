package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller for the home page.
 */
public abstract class SunriseHomeController extends SunriseTemplateController implements WithQueryFlow<Void> {

    private final HomePageContentFactory homePageContentFactory;

    protected SunriseHomeController(final TemplateRenderer templateRenderer,
                                    final HomePageContentFactory homePageContentFactory) {
        super(templateRenderer);
        this.homePageContentFactory = homePageContentFactory;
    }

    @RunRequestStartedHook
    @SunriseRoute("homePageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return homePageContentFactory.create(null);
    }
}
