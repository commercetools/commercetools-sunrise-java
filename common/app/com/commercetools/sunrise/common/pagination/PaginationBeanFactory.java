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
    protected final void initialize(final PaginationBean bean, final Data data) {
        fillPages(bean, data);
        fillPreviousUrl(bean, data);
        fillNextUrl(bean, data);
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

    protected void fillPages(final PaginationBean bean, final Data data) {
        if (canDisplayAllPages(data)) {
            fillWithAllPages(bean, data);
        } else if (data.currentPage < data.thresholdLeft) {
            fillWithLastAndRestPages(bean, data);
        } else if (data.currentPage > data.thresholdRight) {
            fillWithFirstAndRestPages(bean, data);
        } else {
            fillWithFirstLastAndMiddlePages(bean, data);
        }
    }

    private boolean canDisplayAllPages(final Data data) {
        return data.totalPages <= displayedPages;
    }


    // TODO
    protected void fillLastPage(final PaginationBean bean, final Data data) {
        if (!canDisplayAllPages(data) && data.currentPage > data.thresholdRight) {
            bean.setLastPage(createLinkData(data, data.totalPages));
        }
    }

    protected void fillWithAllPages(final PaginationBean bean, final Data data) {
        bean.setPages(createPages(data, 1, data.totalPages));
    }

    protected void fillWithFirstAndRestPages(final PaginationBean bean, final Data data) {
        bean.setPages(createPages(data, data.thresholdRight, data.totalPages));
        bean.setFirstPage(createLinkData(data, 1));
    }

    protected void fillWithLastAndRestPages(final PaginationBean bean, final Data data) {
        bean.setPages(createPages(data, 1, data.thresholdLeft));
        bean.setLastPage(createLinkData(data, data.totalPages));
    }

    protected void fillWithFirstLastAndMiddlePages(final PaginationBean bean, final Data data) {
        bean.setPages(createPages(data, data.currentPage - 1, data.currentPage + 1));
        bean.setFirstPage(createLinkData(data, 1));
        bean.setLastPage(createLinkData(data, data.totalPages));
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
        public final long thresholdLeft;
        public final long thresholdRight;

        public Data(final PagedResult<?> searchResult, final Pagination pagination, final int pageSize, final int displayedPages) {
            this.searchResult = searchResult;
            this.formFieldName = pagination.getKey();
            this.currentPage = pagination.getPage();
            this.pageSize = pageSize;
            this.totalPages = calculateTotalPages(searchResult.getTotal(), pageSize);
            this.thresholdLeft = displayedPages - 1;
            this.thresholdRight = totalPages - displayedPages + 2;
        }

        private static long calculateTotalPages(final float total, final int pageSize) {
            final Double totalPages = Math.ceil(total / pageSize);
            return totalPages.longValue();
        }
    }
}
