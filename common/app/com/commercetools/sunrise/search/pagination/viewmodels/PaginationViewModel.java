package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class PaginationViewModel extends ViewModel {

    private String previousUrl;
    private String nextUrl;
    private PaginationLinkViewModel firstPage;
    private PaginationLinkViewModel lastPage;
    private List<PaginationLinkViewModel> pages;

    public PaginationViewModel() {
    }

    public List<PaginationLinkViewModel> getPages() {
        return pages;
    }

    public void setPages(final List<PaginationLinkViewModel> pages) {
        this.pages = pages;
    }

    public PaginationLinkViewModel getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(final PaginationLinkViewModel firstPage) {
        this.firstPage = firstPage;
    }

    public PaginationLinkViewModel getLastPage() {
        return lastPage;
    }

    public void setLastPage(final PaginationLinkViewModel lastPage) {
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
