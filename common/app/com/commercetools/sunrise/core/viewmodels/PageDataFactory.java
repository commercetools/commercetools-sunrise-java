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

    public OldPageData create(final PageContent content) {
        final OldPageData oldPageData = new OldPageData();
        oldPageData.setFooter(pageFooterFactory.create(content));
        oldPageData.setMeta(pageMetaFactory.create(content));
        oldPageData.setContent(content);
        return oldPageData;
    }
}
