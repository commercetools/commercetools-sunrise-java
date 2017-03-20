package com.commercetools.sunrise.framework.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.viewmodels.footer.PageFooterFactory;
import com.commercetools.sunrise.framework.viewmodels.header.PageHeaderFactory;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMetaFactory;

import javax.inject.Inject;

public final class PageDataFactory {

    private final PageHeaderFactory pageHeaderFactory;
    private final PageFooterFactory pageFooterFactory;
    private final PageMetaFactory pageMetaFactory;

    @Inject
    public PageDataFactory(final PageHeaderFactory pageHeaderFactory, final PageFooterFactory pageFooterFactory, final PageMetaFactory pageMetaFactory) {
        this.pageHeaderFactory = pageHeaderFactory;
        this.pageFooterFactory = pageFooterFactory;
        this.pageMetaFactory = pageMetaFactory;
    }

    public PageData create(final PageContent content) {
        final PageData pageData = new PageData();
        pageData.setHeader(pageHeaderFactory.create(content));
        pageData.setFooter(pageFooterFactory.create(content));
        pageData.setMeta(pageMetaFactory.create(content));
        pageData.setContent(content);
        return pageData;
    }
}
