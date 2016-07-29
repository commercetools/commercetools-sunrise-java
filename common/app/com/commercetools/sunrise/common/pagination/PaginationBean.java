package com.commercetools.sunrise.common.pagination;

import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class PaginationBean extends ModelBean {

    private String previousUrl;
    private String nextUrl;
    private LinkBean firstPage;
    private LinkBean lastPage;
    private List<LinkBean> pages;

    public PaginationBean() {
    }

    public List<LinkBean> getPages() {
        return pages;
    }

    public void setPages(final List<LinkBean> pages) {
        this.pages = pages;
    }

    public LinkBean getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(final LinkBean firstPage) {
        this.firstPage = firstPage;
    }

    public LinkBean getLastPage() {
        return lastPage;
    }

    public void setLastPage(final LinkBean lastPage) {
        this.lastPage = lastPage;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(final String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(final String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
