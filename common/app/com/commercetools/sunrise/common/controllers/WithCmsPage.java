package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.contexts.UserContext;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface WithCmsPage {

    default CompletionStage<Optional<CmsPage>> cmsPage() {
        return cmsService().page(getCmsPageKey(), userContext().locales());
    }

    String getCmsPageKey();

    CmsService cmsService();

    UserContext userContext();

}
