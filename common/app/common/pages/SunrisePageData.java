package common.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;

public final class SunrisePageData implements PageData {
    private final CmsPage cms;
    private final AppContext context;
    private final PageContent pageContent;

    private SunrisePageData(final CmsPage cms, final AppContext context, final PageContent pageContent) {
        this.cms = cms;
        this.context = context;
        this.pageContent = pageContent;
    }

    @Override
    public PageHeader getHeader() {
        final String title = cms.getOrEmpty("header.title") + pageContent.additionalTitle();
        return new PageHeader(cms, context, title);
    }

    @Override
    public PageContent getContent() {
        return pageContent;
    }

    @Override
    public PageFooter getFooter() {
        return new PageFooter();
    }

    @Override
    public SeoData getSeo() {
        return new SeoData();
    }

    @Override
    public PageMeta getMeta() {
        return new PageMeta();
    }

    public static SunrisePageData of(final CmsPage cms, final AppContext context, final PageContent pageContent) {
        return new SunrisePageData(cms, context, pageContent);
    }
}
