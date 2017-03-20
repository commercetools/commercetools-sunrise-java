package com.commercetools.sunrise.framework.template.engine;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.framework.viewmodels.PageData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class TemplateContext {

    private final PageData pageData;
    private final List<Locale> locales;
    @Nullable
    private final CmsPage cmsPage;

    public TemplateContext(final PageData pageData, final List<Locale> locales, @Nullable final CmsPage cmsPage) {
        this.pageData = pageData;
        this.locales = locales;
        this.cmsPage = cmsPage;
    }

    public PageData pageData() {
        return pageData;
    }

    public List<Locale> locales() {
        return locales;
    }

    public Optional<CmsPage> cmsPage() {
        return Optional.ofNullable(cmsPage);
    }
}
