package com.commercetools.sunrise.cms;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

public final class CmsPageComponent implements ControllerComponent, PageDataHook {

    private final CmsService cmsService;
    private final Provider<Locale> localeProvider;

    @Inject
    CmsPageComponent(final CmsService cmsService, final Provider<Locale> localeProvider) {
        this.cmsService = cmsService;
        this.localeProvider = localeProvider;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return cmsService.page("default", singletonList(localeProvider.get()))
                .thenApply(cmsPageOpt -> cmsPageOpt
                        .map(cmsPage -> pageData.put("cmsPage", cmsPage))
                        .orElse(pageData));
    }
}
