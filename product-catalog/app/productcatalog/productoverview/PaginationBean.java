package productcatalog.productoverview;

import common.contexts.RequestContext;
import common.models.LinkBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;

import java.util.List;
import java.util.stream.LongStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class PaginationBean extends Base {
    private String previousUrl;
    private String nextUrl;
    private LinkBean firstPage;
    private LinkBean lastPage;
    private List<LinkBean> pages;

    public PaginationBean() {
    }

    public PaginationBean(final RequestContext requestContext, final PagedResult<?> searchResult,
                          final int currentPage, final int pageSize, final int displayedPages) {
        final long totalPages = calculateTotalPages(searchResult, pageSize);
        final long thresholdLeft = displayedPages - 1;
        final long thresholdRight = totalPages - displayedPages + 2;

        if (totalPages <= displayedPages) {
            this.pages = createPages(1, totalPages, currentPage, requestContext);
        } else if (currentPage < thresholdLeft) {
            this.pages = createPages(1, thresholdLeft, currentPage, requestContext);
            this.lastPage = createLinkData(totalPages, currentPage, requestContext);
        } else if (currentPage > thresholdRight) {
            this.pages = createPages(thresholdRight, totalPages, currentPage, requestContext);
            this.firstPage = createLinkData(1, currentPage, requestContext);
        } else {
            this.pages = createPages(currentPage - 1, currentPage + 1, currentPage, requestContext);
            this.firstPage = createLinkData(1, currentPage, requestContext);
            this.lastPage = createLinkData(totalPages, currentPage, requestContext);
        }
        if (!searchResult.isFirst()) {
            this.previousUrl = buildRequestUrlWithPage(currentPage - 1, requestContext);
        }
        if (!searchResult.isLast()) {
            this.nextUrl = buildRequestUrlWithPage(currentPage + 1, requestContext);
        }
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

    private static long calculateTotalPages(final PagedResult<?> searchResult, final int pageSize) {
        final Double totalPages = Math.ceil((float) searchResult.getTotal() / pageSize);
        return totalPages.longValue();
    }

    private static List<LinkBean> createPages(final long startPage, final long endPage, final long currentPage,
                                              final RequestContext requestContext) {
        return LongStream.rangeClosed(startPage, endPage)
                .mapToObj(page -> createLinkData(page, currentPage, requestContext))
                .collect(toList());
    }

    private static LinkBean createLinkData(final long page, final long currentPage, final RequestContext requestContext) {
        final LinkBean linkBean = new LinkBean(String.valueOf(page), buildRequestUrlWithPage(page, requestContext));
        if (page == currentPage) {
            linkBean.setSelected(true);
        }
        return linkBean;
    }

    private static String buildRequestUrlWithPage(final long page, final RequestContext requestContext) {
        return requestContext.buildUrl("page", singletonList(String.valueOf(page)));
    }
}
