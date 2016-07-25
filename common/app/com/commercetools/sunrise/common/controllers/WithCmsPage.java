package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.cms.CmsPage;
import com.commercetools.sunrise.common.template.cms.CmsService;

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
