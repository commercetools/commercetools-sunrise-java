package com.commercetools.sunrise.common.pagination;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class PaginationBeanFactory extends ViewModelFactory<PaginationBean, PaginationBeanFactory.Data> {

    private final int displayedPages;
    private final RequestContext requestContext;

    @Inject
    public PaginationBeanFactory(final Configuration configuration, final RequestContext requestContext) {
        this.displayedPages = configuration.getInt("pop.pagination.displayedPages", 6);
        this.requestContext = requestContext;
    }

    public final PaginationBean create(final PagedResult<?> searchResult, final Pagination pagination, final int pageSize) {
        final Data data = new Data(searchResult, pagination, pageSize, displayedPages);
        return initializedViewModel(data);
    }

    @Override
    protected PaginationBean getViewModelInstance() {
        return new PaginationBean();
    }

    @Override
    protected final void initialize(final PaginationBean bean, final Data data) {
        fillNextUrl(bean, data);
        fillPreviousUrl(bean, data);
        fillFirstPage(bean, data);
        fillLastPage(bean, data);
        fillPages(bean, data);
    }

    protected void fillNextUrl(final PaginationBean bean, final Data data) {
        if (!data.searchResult.isLast()) {
            bean.setNextUrl(buildUrlWithPage(data.formFieldName, data.currentPage + 1));
        }
    }

    protected void fillPreviousUrl(final PaginationBean bean, final Data data) {
        if (!data.searchResult.isFirst()) {
            bean.setPreviousUrl(buildUrlWithPage(data.formFieldName, data.currentPage - 1));
        }
    }

    protected void fillFirstPage(final PaginationBean bean, final Data data) {
        if (firstPageIsDisplayed(data)) {
            bean.setFirstPage(createLinkData(data, 1));
        }
    }

    protected void fillLastPage(final PaginationBean bean, final Data data) {
        if (lastPageIsDisplayed(data)) {
            bean.setLastPage(createLinkData(data, data.totalPages));
        }
    }

    protected void fillPages(final PaginationBean bean, final Data data) {
        long startPage = 1;
        long endPage = data.totalPages;
        if (firstPageIsDisplayed(data) && lastPageIsDisplayed(data)) {
            startPage = data.currentPage - 1;
            endPage = data.currentPage + 1;
        } else if (firstPageIsDisplayed(data)) {
            startPage = data.topThreshold;
        } else if (lastPageIsDisplayed(data)) {
            endPage = data.bottomThreshold;
        }
        bean.setPages(createPages(data, startPage, endPage));
    }

    private boolean notAllPagesAreDisplayed(final Data data) {
        return data.totalPages > displayedPages;
    }

    private boolean firstPageIsDisplayed(final Data data) {
        final boolean currentPageIsAboveBottomThreshold = data.currentPage > data.bottomThreshold;
        return notAllPagesAreDisplayed(data) && currentPageIsAboveBottomThreshold;
    }

    private boolean lastPageIsDisplayed(final Data data) {
        final boolean currentPageIsBelowTopThreshold = data.currentPage < data.topThreshold;
        return notAllPagesAreDisplayed(data) && currentPageIsBelowTopThreshold;
    }

    private List<LinkBean> createPages(final Data data, final long startPage, final long endPage) {
        return LongStream.rangeClosed(startPage, endPage)
                .mapToObj(page -> createLinkData(data, page))
                .collect(toList());
    }

    private LinkBean createLinkData(final Data data, final long page) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(String.valueOf(page));
        linkBean.setUrl(buildUrlWithPage(data.formFieldName, page));
        if (page == data.currentPage) {
            linkBean.setSelected(true);
        }
        return linkBean;
    }

    private String buildUrlWithPage(final String key, final long page) {
        return requestContext.buildUrl(key, singletonList(String.valueOf(page)));
    }

    protected final static class Data extends Base {

        public final PagedResult<?> searchResult;
        public final String formFieldName;
        public final int currentPage;
        public final int pageSize;
        public final long totalPages;
        public final long bottomThreshold;
        public final long topThreshold;

        public Data(final PagedResult<?> searchResult, final Pagination pagination, final int pageSize, final int displayedPages) {
            this.searchResult = searchResult;
            this.formFieldName = pagination.getKey();
            this.currentPage = pagination.getPage();
            this.pageSize = pageSize;
            this.totalPages = calculateTotalPages(searchResult.getTotal(), pageSize);
            this.bottomThreshold = displayedPages - 1;
            this.topThreshold = totalPages - displayedPages + 2;
        }

        private static long calculateTotalPages(final float total, final int pageSize) {
            final Double totalPages = Math.ceil(total / pageSize);
            return totalPages.longValue();
        }
    }
}
