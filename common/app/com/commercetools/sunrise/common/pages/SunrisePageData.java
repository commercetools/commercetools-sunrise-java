package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.pages.*;
import io.sphere.sdk.models.Base;

public final class SunrisePageData extends Base implements PageData {

    private PageHeader header;
    private PageFooter footer;
    private PageContent content;
    private PageMeta meta;

    public SunrisePageData() {
    }

    @Override
    public PageHeader getHeader() {
        return header;
    }

    public void setHeader(final PageHeader header) {
        this.header = header;
    }

    @Override
    public PageFooter getFooter() {
        return footer;
    }

    public void setFooter(final PageFooter footer) {
        this.footer = footer;
    }

    @Override
    public PageContent getContent() {
        return content;
    }

    public void setContent(final PageContent content) {
        this.content = content;
    }

    @Override
    public PageMeta getMeta() {
        return meta;
    }

    public void setMeta(final PageMeta meta) {
        this.meta = meta;
    }
}
