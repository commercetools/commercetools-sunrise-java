package common.pages;

import common.cms.CmsPage;

public final class SunrisePageData implements PageData {
    private final CmsPage cms;
    private final PageContent pageContent;

    private SunrisePageData(final CmsPage cms, final PageContent pageContent) {
        this.cms = cms;
        this.pageContent = pageContent;
    }

    @Override
    public PageHeader getHeader() {
        final String title = cms.getOrEmpty("header.baseTitle") + pageContent.additionalTitle();
        return new PageHeader(cms, title);
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

    public static SunrisePageData of(final CmsPage cms, final PageContent pageContent) {
        return new SunrisePageData(cms, pageContent);
    }
}
