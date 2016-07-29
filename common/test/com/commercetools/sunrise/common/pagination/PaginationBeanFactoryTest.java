package com.commercetools.sunrise.common.pagination;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.models.LinkBean;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;
import org.junit.Test;
import play.Configuration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class PaginationBeanFactoryTest {
    private static final String URL_PATH = "www.url.dom/path/to/";
    private static final Long PAGE_SIZE = 9L;

    @Test
    public void calculatesPagination() throws Exception {
        final int page = 3;
        final int totalPages = 5;
        final int displayedPages = 5;
        final PaginationBean pagination = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(pagination.getPreviousUrl()).isEqualTo(urlWithPage(2));
        assertThat(pagination.getNextUrl()).isEqualTo(urlWithPage(4));
        assertThat(pagination.getFirstPage()).isNull();
        assertThat(pagination.getLastPage()).isNull();
        assertThat(pagination.getPages())
                .extracting(LinkBean::getText)
                .containsExactly("1", "2", "3", "4", "5");
        assertThat(pagination.getPages())
                .extracting(LinkBean::isSelected)
                .containsExactly(false, false, true, false, false);
    }

    @Test
    public void calculatesPaginationForFirstPage() throws Exception {
        final int page = 1;
        final int totalPages = 10;
        final int displayedPages = 5;
        final PaginationBean pagination = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(pagination.getPreviousUrl()).isNull();
        assertThat(pagination.getNextUrl()).isEqualTo(urlWithPage(2));
        assertThat(pagination.getFirstPage()).isNull();
        assertThat(pagination.getLastPage().getText()).isEqualTo("10");
    }

    @Test
    public void calculatesPaginationForLastPage() throws Exception {
        final int page = 10;
        final int totalPages = 10;
        final int displayedPages = 5;
        final PaginationBean pagination = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
        assertThat(pagination.getPreviousUrl()).isEqualTo(urlWithPage(9));
        assertThat(pagination.getNextUrl()).isNull();
        assertThat(pagination.getFirstPage().getText()).isEqualTo("1");
        assertThat(pagination.getLastPage()).isNull();
    }

    @Test
    public void calculatesPaginationForAllFirstPages() throws Exception {
        final int totalPages = 10;
        final int displayedPages = 5;
        IntStream.rangeClosed(1, 3)
                .forEach(page -> {
                    final PaginationBean pagination = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
                    assertThat(pagination.getPages())
                            .extracting(LinkBean::getText)
                            .containsExactly("1", "2", "3", "4");
                });
    }

    @Test
    public void calculatesPaginationForAllLastPages() throws Exception {
        final int displayedPages = 5;
        final int totalPages = 10;
        IntStream.rangeClosed(8, 10)
                .forEach(page -> {
                    final PaginationBean pagination = createPaginationData(page, displayedPages, pagedResult(page, totalPages));
                    assertThat(pagination.getPages())
                            .extracting(LinkBean::getText)
                            .containsExactly("7", "8", "9", "10");
                });
    }

    private String urlWithPage(final int page) {
        return URL_PATH + "?foo=bar&page=" + page;
    }

    private PaginationBean createPaginationData(final int currentPage, final int displayedPages, final PagedResult<ProductProjection> searchResult) {
        final RequestContext requestContext = RequestContext.of(buildQueryString(currentPage), URL_PATH);
        final Map<String, Object> configMap = singletonMap("pop.pagination.displayedPages", displayedPages);
        final Configuration configuration = new Configuration(configMap);
        final Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(final Binder binder) {
                binder.bind(RequestContext.class).toInstance(requestContext);
                binder.bind(Configuration.class).toInstance(configuration);
            }
        });
        final Pagination pagination = Pagination.of("page", currentPage);
        return injector.getInstance(PaginationBeanFactory.class).create(searchResult, pagination, PAGE_SIZE.intValue());
    }

    private PagedResult<ProductProjection> pagedResult(final int page, final int totalPages) {
        final long offset = (page - 1) * PAGE_SIZE;
        final long totalProducts = totalPages * PAGE_SIZE;
        final List<ProductProjection> products = Collections.nCopies(PAGE_SIZE.intValue(), null);
        return new PagedResult<ProductProjection>() {
            @Override
            public List<ProductProjection> getResults() {
                return products;
            }

            @Override
            public Long getOffset() {
                return offset;
            }

            @Override
            public Long getTotal() {
                return totalProducts;
            }

            @Override
            public Long size() {
                return getCount();
            }

            @Override
            public Long getCount() {
                return PAGE_SIZE;
            }
        };
    }

    private Map<String, List<String>> buildQueryString(final int currentPage) {
        final LinkedHashMap<String, List<String>> queryString = new LinkedHashMap<>();
        queryString.put("foo", singletonList("bar"));
        queryString.put("page", singletonList(String.valueOf(currentPage)));
        return queryString;
    }
}
