package productcatalog.pages;

import common.pages.SelectableLinkData;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static common.utils.UrlUtils.buildUrl;
import static java.util.stream.Collectors.toList;

public class PaginationDataFactory extends Base {
    private final Http.Request request;
    private final PagedResult<?> searchResult;
    private final int currentPage;
    private final int totalPages;
    private final int productsCount;
    private final int totalProducts;
    private final int displayedPages;
    private int pageThresholdLeft;
    private int pageThresholdRight;

    public PaginationDataFactory(final Http.Request request, final PagedResult<?> searchResult,
                                 final int currentPage, final int pageSize, final int displayedPages) {
        this.request = request;
        this.searchResult = searchResult;
        this.currentPage = currentPage;
        this.totalPages = getTotalPages(searchResult, pageSize);
        this.productsCount = searchResult.getOffset() + searchResult.size();
        this.totalProducts = searchResult.getTotal();
        this.displayedPages = displayedPages;
        this.pageThresholdLeft = displayedPages - 1;
        this.pageThresholdRight = totalPages - displayedPages + 2;
    }

    public PaginationData create() {
        final PaginationData paginationData = new PaginationData(productsCount, totalProducts);

        final List<SelectableLinkData> pages;
        if (totalPages <= displayedPages) {
            pages = getPages(1, totalPages);
        } else if (currentPage < pageThresholdLeft) {
            pages = getPages(1, pageThresholdLeft);
            paginationData.setLastPage(getLinkData(totalPages));
        } else if (currentPage > pageThresholdRight) {
            pages = getPages(pageThresholdRight, totalPages);
            paginationData.setFirstPage(getLinkData(1));
        } else {
            pages = getPages(currentPage - 1, currentPage + 1);
            paginationData.setFirstPage(getLinkData(1));
            paginationData.setLastPage(getLinkData(totalPages));
        }
        paginationData.setPages(pages);

        if (!searchResult.isFirst()) {
            paginationData.setPrevPage(getLinkData(currentPage - 1));
        }
        if (!searchResult.isLast()) {
            paginationData.setNextPage(getLinkData(currentPage + 1));
        }
        return paginationData;
    }

    private List<SelectableLinkData> getPages(final int startPage, final int endPage) {
        return IntStream.rangeClosed(startPage, endPage)
                .mapToObj(this::getLinkData)
                .collect(toList());
    }

    private SelectableLinkData getLinkData(final int page) {
        return new SelectableLinkData(String.valueOf(page), buildRequestUrlWithPage(page), page == currentPage);
    }

    private String buildRequestUrlWithPage(final int page) {
        final Map<String, String[]> queryString = new HashMap<>(request.queryString());
        queryString.put("page", new String[]{String.valueOf(page)});
        return buildUrl(request.path(), queryString);
    }

    private static int getTotalPages(final PagedResult<?> searchResult, final int pageSize) {
        final Double totalPages = Math.ceil((float) searchResult.getTotal() / pageSize);
        return totalPages.intValue();
    }
}
