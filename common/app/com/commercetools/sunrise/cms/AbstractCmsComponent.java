package com.commercetools.sunrise.cms;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestStartedHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Http;

import javax.inject.Provider;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

public abstract class AbstractCmsComponent implements ControllerComponent, HttpRequestStartedHook, PageDataHook {

    private final CmsService cmsService;
    private final Provider<Locale> localeProvider;

    private CompletionStage<Optional<CmsPage>> cmsPageStage;

    protected AbstractCmsComponent(final CmsService cmsService, final Provider<Locale> localeProvider) {
        this.cmsService = cmsService;
        this.localeProvider = localeProvider;
    }

    @Override
    public void onHttpRequestStarted(final Http.Context httpContext) {
        cmsPageStage = cmsService.page(pageKey(), singletonList(localeProvider.get()));
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return cmsPageStage.thenApply(cmsPageOpt -> cmsPageOpt
                .map(cmsPage -> pageData.put("cms", cmsPage))
                .orElse(pageData));
    }

    protected abstract String pageKey();
}
