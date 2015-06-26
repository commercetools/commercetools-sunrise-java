package common.pages;

import common.cms.CmsPage;
import common.contexts.ProjectContext;

public final class SunrisePageData implements PageData {
    private final CmsPage cms;
    private final ProjectContext projectContext;
    private final PageContent pageContent;

    private SunrisePageData(final CmsPage cms, final ProjectContext projectContext, final PageContent pageContent) {
        this.cms = cms;
        this.projectContext = projectContext;
        this.pageContent = pageContent;
    }

    @Override
    public PageHeader getHeader() {
        final String title = cms.getOrEmpty("header.title") + pageContent.additionalTitle();
        return new PageHeader(cms, projectContext, title);
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

    public static SunrisePageData of(final CmsPage cms, final ProjectContext projectContext, final PageContent pageContent) {
        return new SunrisePageData(cms, projectContext, pageContent);
    }
}
