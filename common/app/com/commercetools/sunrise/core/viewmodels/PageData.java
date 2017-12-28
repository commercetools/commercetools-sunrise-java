package com.commercetools.sunrise.core.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.core.viewmodels.footer.PageFooter;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

public final class PageData extends ViewModel {

    private PageFooter footer;
    private PageContent content;
    private PageMeta meta;

    public PageData() {
    }

    public PageFooter getFooter() {
        return footer;
    }

    public void setFooter(final PageFooter footer) {
        this.footer = footer;
    }

    public PageContent getContent() {
        return content;
    }

    public void setContent(final PageContent content) {
        this.content = content;
    }

    public PageMeta getMeta() {
        return meta;
    }

    public void setMeta(final PageMeta meta) {
        this.meta = meta;
    }
}
