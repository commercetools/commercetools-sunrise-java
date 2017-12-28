package com.commercetools.sunrise.core.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.core.viewmodels.footer.PageFooterFactory;
import com.commercetools.sunrise.core.viewmodels.meta.PageMetaFactory;

import javax.inject.Inject;

public final class PageDataFactory {

    private final PageFooterFactory pageFooterFactory;
    private final PageMetaFactory pageMetaFactory;

    @Inject
    public PageDataFactory(final PageFooterFactory pageFooterFactory, final PageMetaFactory pageMetaFactory) {
        this.pageFooterFactory = pageFooterFactory;
        this.pageMetaFactory = pageMetaFactory;
    }

    public PageData create(final PageContent content) {
        final PageData pageData = new PageData();
        pageData.setFooter(pageFooterFactory.create(content));
        pageData.setMeta(pageMetaFactory.create(content));
        pageData.setContent(content);
        return pageData;
    }
}
