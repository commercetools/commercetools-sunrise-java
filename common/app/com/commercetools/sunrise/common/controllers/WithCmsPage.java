package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.template.cms.CmsPage;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface WithCmsPage {

    CompletionStage<Optional<CmsPage>> cmsPage();
}
