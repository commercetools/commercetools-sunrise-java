package productcatalog.models;

import common.contexts.RequestContext;
import common.models.LinkData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PaginationDataTest {
    private static final String URL_PATH = "www.url.dom/path/to/";
    private static final int PAGE_SIZE = 9;

    @Test
    public void calculatesPagination() throws Exception {
        final int page = 3;
        final int totalPages = 5;
        final int displayedPages = 5;
        final PaginationData paginationData = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(paginationData.getPreviousUrl()).isEqualTo(urlWithPage(2));
        assertThat(paginationData.getNextUrl()).isEqualTo(urlWithPage(4));
        assertThat(paginationData.getFirstPage()).isNull();
        assertThat(paginationData.getLastPage()).isNull();
        assertThat(paginationData.getPages())
                .extracting(LinkData::getText)
                .containsExactly("1", "2", "3", "4", "5");
        assertThat(paginationData.getPages())
                .extracting(LinkData::isSelected)
                .containsExactly(false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForFirstPage() throws Exception {
        final int page = 1;
        final int totalPages = 10;
        final int displayedPages = 5;
        final PaginationData paginationData = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(paginationData.getPreviousUrl()).isNull();
        assertThat(paginationData.getNextUrl()).isEqualTo(urlWithPage(2));
        assertThat(paginationData.getFirstPage()).isNull();
        assertThat(paginationData.getLastPage().getText()).isEqualTo("10");
    }

    @Test
    public void calculatesPaginationForLastPage() throws Exception {
        final int page = 10;
        final int totalPages = 10;
        final int displayedPages = 5;
        final PaginationData paginationData = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(paginationData.getPreviousUrl()).isEqualTo(urlWithPage(9));
        assertThat(paginationData.getNextUrl()).isNull();
        assertThat(paginationData.getFirstPage().getText()).isEqualTo("1");
        assertThat(paginationData.getLastPage()).isNull();
    }

    @Test
    public void calculatesPaginationForAllFirstPages() throws Exception {
        final int totalPages = 10;
        final int displayedPages = 5;
        IntStream.rangeClosed(1, 3)
                .forEach(page -> {
                    final PaginationData paginationData = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
                    assertThat(paginationData.getPages())
                            .extracting(LinkData::getText)
                            .containsExactly("1", "2", "3", "4");
                });
    }

    @Test
    public void calculatesPaginationForAllLastPages() throws Exception {
        final int displayedPages = 5;
        final int totalPages = 10;
        IntStream.rangeClosed(8, 10)
                .forEach(page -> {
                    final PaginationData paginationData = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
                    assertThat(paginationData.getPages())
                            .extracting(LinkData::getText)
                            .containsExactly("7", "8", "9", "10");
                });
    }

    private String urlWithPage(final int page) {
        return URL_PATH + "?foo=bar&page=" + page;
    }

    private PaginationData createPaginationData(final int currentPage, final int displayedPages, final PagedResult<ProductProjection> searchResult) {
        final RequestContext requestContext = RequestContext.of(buildQueryString(currentPage), URL_PATH);
        return new PaginationData(requestContext, searchResult, currentPage, PAGE_SIZE, displayedPages);
    }

    @SuppressWarnings("unchecked")
    private PagedResult<ProductProjection> pagedResult(final int page, final int totalPages) {
        final long offset = (page - 1) * PAGE_SIZE;
        final long totalProducts = totalPages * PAGE_SIZE;
        final List<ProductProjection> products = Collections.nCopies(PAGE_SIZE, null);
        return new PagedResult(offset, totalProducts, products) {};
    }

    private Map<String, String[]> buildQueryString(final int currentPage) {
        final LinkedHashMap<String, String[]> queryString = new LinkedHashMap<>();
        queryString.put("foo", new String[] {"bar"});
        queryString.put("page", new String[] {String.valueOf(currentPage)});
        return queryString;
    }
}
