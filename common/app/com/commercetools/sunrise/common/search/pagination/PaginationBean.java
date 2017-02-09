package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class PaginationBean extends ViewModel {

    private String previousUrl;
    private String nextUrl;
    private PaginationLinkBean firstPage;
    private PaginationLinkBean lastPage;
    private List<PaginationLinkBean> pages;

    public PaginationBean() {
    }

    public List<PaginationLinkBean> getPages() {
        return pages;
    }

    public void setPages(final List<PaginationLinkBean> pages) {
        this.pages = pages;
    }

    public PaginationLinkBean getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(final PaginationLinkBean firstPage) {
        this.firstPage = firstPage;
    }

    public PaginationLinkBean getLastPage() {
        return lastPage;
    }

    public void setLastPage(final PaginationLinkBean lastPage) {
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
