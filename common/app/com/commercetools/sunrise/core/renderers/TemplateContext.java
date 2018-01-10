package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.viewmodels.OldPageData;

import javax.annotation.Nullable;
import java.util.Optional;

public class TemplateContext {

    private final OldPageData oldPageData;
    @Nullable
    private final CmsPage cmsPage;

    public TemplateContext(final OldPageData oldPageData, @Nullable final CmsPage cmsPage) {
        this.oldPageData = oldPageData;
        this.cmsPage = cmsPage;
    }

    public OldPageData pageData() {
        return oldPageData;
    }

    public Optional<CmsPage> cmsPage() {
        return Optional.ofNullable(cmsPage);
    }
}
