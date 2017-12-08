package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils;
import com.commercetools.sunrise.search.pagination.PaginationSettings;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.mvc.Http;
import play.test.WithApplication;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.commercetools.sunrise.search.pagination.viewmodels.AbstractPaginationViewModelFactory.calculateTotalPages;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPaginationViewModelFactoryTest extends WithApplication {
    private static final String URL_PATH = "www.url.dom/path/to/";
    private static final int PAGE_SIZE = 9;

    @Mock
    private PagedResult<ProductProjection> pagedResult;

    @Before
    public void setUp() throws Exception {
        when(pagedResult.isFirst()).thenCallRealMethod();
        when(pagedResult.isLast()).thenCallRealMethod();
    }

    @Test
    public void calculatesPagination() throws Exception {
        final int page = 3;
        final int totalPages = 5;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getPreviousUrl()).isEqualTo(urlWithPage(2));
        assertThat(pagination.getNextUrl()).isEqualTo(urlWithPage(4));
        assertThat(pagination.getFirstPage()).isNull();
        assertThat(pagination.getLastPage()).isNull();
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("1", "2", "3", "4", "5");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForFirstPage() throws Exception {
        final int page = 1;
        final int totalPages = 10;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getPreviousUrl()).isNull();
        assertThat(pagination.getNextUrl()).isEqualTo(urlWithPage(2));
        assertThat(pagination.getFirstPage()).isNull();
        assertThat(pagination.getLastPage().getText()).isEqualTo("10");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("1", "2", "3");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(true, false, false);
    }

    @Test
    public void calculatesPaginationForLastPage() throws Exception {
        final int page = 10;
        final int totalPages = 10;
        final int displayedPages = 2;

        Stream.of(1, 2, PAGE_SIZE).forEach(count -> {
            mockPagedResult(page, count, totalPages);
            final PaginationViewModel pagination = createPaginationData(page, displayedPages);
            assertThat(pagination.getPreviousUrl()).isEqualTo(urlWithPage(9));
            assertThat(pagination.getNextUrl()).isNull();
            assertThat(pagination.getFirstPage().getText()).isEqualTo("1");
            assertThat(pagination.getLastPage()).isNull();
            assertThat(pagination.getPages())
                    .extracting(PaginationLinkViewModel::getText)
                    .containsExactly("8", "9", "10");
            assertThat(pagination.getPages())
                    .extracting(PaginationLinkViewModel::isSelected)
                    .containsExactly(false, false, true);
        });
    }

    @Test
    public void calculatesPaginationForFourthPage() throws Exception {
        final int page = 4;
        final int totalPages = 10;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getFirstPage()).isNull();
        assertThat(pagination.getLastPage()).isNotNull();
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("1", "2", "3", "4", "5", "6");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(false, false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForFifthPage() throws Exception {
        final int page = 5;
        final int totalPages = 10;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getFirstPage()).isNotNull();
        assertThat(pagination.getLastPage()).isNotNull();
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("3", "4", "5", "6", "7");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForSixthPage() throws Exception {
        final int page = 6;
        final int totalPages = 10;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getFirstPage()).isNotNull();
        assertThat(pagination.getLastPage()).isNotNull();
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("4", "5", "6", "7", "8");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForSeventhPage() throws Exception {
        final int page = 7;
        final int totalPages = 10;
        final int displayedPages = 2;
        mockPagedResultWithNonLastPagePagedResult(page, totalPages);
        final PaginationViewModel pagination = createPaginationData(page, displayedPages);
        assertThat(pagination.getFirstPage()).isNotNull();
        assertThat(pagination.getLastPage()).isNull();
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::getText)
                .containsExactly("5", "6", "7", "8", "9", "10");
        assertThat(pagination.getPages())
                .extracting(PaginationLinkViewModel::isSelected)
                .containsExactly(false, false, true, false, false, false);
    }

    @Test
    public void calculatesTotalPagesForMiddlePage() throws Exception {
        when(pagedResult.getTotal()).thenReturn(44L);
        when(pagedResult.getCount()).thenReturn(12L);
        when(pagedResult.getOffset()).thenReturn(12L);
        assertThat(calculateTotalPages(pagedResult, 2)).isEqualTo(4);
    }

    @Test
    public void calculatesTotalPagesForFirstPage() throws Exception {
        when(pagedResult.getTotal()).thenReturn(44L);
        when(pagedResult.getCount()).thenReturn(12L);
        when(pagedResult.getOffset()).thenReturn(0L);
        assertThat(calculateTotalPages(pagedResult, 1)).isEqualTo(4);
    }

    @Test
    public void calculatesTotalPagesForLastPage() throws Exception {
        when(pagedResult.getTotal()).thenReturn(44L);
        when(pagedResult.getCount()).thenReturn(12L);
        when(pagedResult.getOffset()).thenReturn(36L);
        assertThat(calculateTotalPages(pagedResult, 4)).isEqualTo(4);
    }

    @Test
    public void calculatesTotalPagesForEmptyResults() throws Exception {
        when(pagedResult.getTotal()).thenReturn(0L);
        when(pagedResult.getCount()).thenReturn(0L);
        when(pagedResult.getOffset()).thenReturn(0L);
        assertThat(calculateTotalPages(pagedResult, 1)).isEqualTo(1);
    }

    private String urlWithPage(final int page) {
        return URL_PATH + "?foo=bar&page=" + page;
    }

    private PaginationViewModel createPaginationData(final int currentPage, final int displayedPages) {
        final Http.Context context = new Http.Context(new Http.RequestBuilder()
                .uri(QueryStringUtils.buildUri(URL_PATH, buildQueryString(currentPage)))
                .build());
        Http.Context.current.set(context);
        final PaginationSettings settings = PaginationSettings.of("page", displayedPages);
        return new TestablePaginationViewModelFactory(settings).create(pagedResult, currentPage);
    }

    private void mockPagedResultWithNonLastPagePagedResult(final int page, final int totalPages) {
        mockPagedResult(page, PAGE_SIZE, totalPages);
    }

    private void mockPagedResult(final int page, final int count, final int totalPages) {
        when(pagedResult.getOffset()).thenReturn(Long.valueOf((page - 1) * PAGE_SIZE));
        when(pagedResult.getTotal()).thenReturn(Long.valueOf((totalPages - 1) * PAGE_SIZE + count));
        when(pagedResult.getResults()).thenReturn(Collections.nCopies(count, null));
        when(pagedResult.getCount()).thenReturn(Long.valueOf(count));
    }

    private Map<String, List<String>> buildQueryString(final int currentPage) {
        final Map<String, List<String>> queryString = new LinkedHashMap<>();
        queryString.put("foo", singletonList("bar"));
        queryString.put("page", singletonList(String.valueOf(currentPage)));
        return queryString;
    }

    private static class TestablePaginationViewModelFactory extends AbstractPaginationViewModelFactory {

        TestablePaginationViewModelFactory(final PaginationSettings settings) {
            super(settings);
        }
    }
}
