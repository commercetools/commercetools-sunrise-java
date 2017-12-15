package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.annotation.Nullable;
import java.util.Optional;

public class TemplateContext {

    private final PageData pageData;
    @Nullable
    private final CmsPage cmsPage;

    public TemplateContext(final PageData pageData, @Nullable final CmsPage cmsPage) {
        this.pageData = pageData;
        this.cmsPage = cmsPage;
    }

    public PageData pageData() {
        return pageData;
    }

    public Optional<CmsPage> cmsPage() {
        return Optional.ofNullable(cmsPage);
    }
}
