package common.pages;

import io.sphere.sdk.models.Base;

public final class SunrisePageData extends Base implements PageData {
    private final PageHeader pageHeader;
    private final PageFooter pageFooter;
    private final PageContent pageContent;
    private final SeoData seoData;
    private final PageMeta pageMeta;

    SunrisePageData(final PageHeader pageHeader, final PageFooter pageFooter, final PageContent pageContent,
                    final SeoData seoData, final PageMeta pageMeta) {
        this.pageHeader = pageHeader;
        this.pageFooter = pageFooter;
        this.pageContent = pageContent;
        this.seoData = seoData;
        this.pageMeta = pageMeta;
    }

    @Override
    public PageHeader getHeader() {
        return pageHeader;
    }

    @Override
    public PageContent getContent() {
        return pageContent;
    }

    @Override
    public PageFooter getFooter() {
        return pageFooter;
    }

    @Override
    public SeoData getSeo() {
        return seoData;
    }

    @Override
    public PageMeta getMeta() {
        return pageMeta;
    }
}
