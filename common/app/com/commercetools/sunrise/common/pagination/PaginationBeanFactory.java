package com.commercetools.sunrise.common.pagination;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.models.LinkBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class PaginationBeanFactory extends Base {

    @Inject
    private Configuration configuration;
    @Inject
    private RequestContext requestContext;

    public PaginationBean create(final PagedResult<?> searchResult, final Pagination pagination, final int pageSize) {
        final PaginationBean bean = new PaginationBean();
        initialize(bean, searchResult, pagination, pageSize);
        return bean;
    }

    protected final void initialize(final PaginationBean bean, final PagedResult<?> searchResult, final Pagination pagination, final int pageSize) {
        fillPages(bean, searchResult, pagination, pageSize);
        fillPreviousUrl(bean, searchResult, pagination);
        fillNextUrl(bean, searchResult, pagination);
    }

    protected void fillNextUrl(final PaginationBean bean, final PagedResult<?> searchResult, final Pagination pagination) {
        if (!searchResult.isLast()) {
            bean.setNextUrl(buildUrlWithPage(pagination.getKey(), pagination.getPage() + 1));
        }
    }

    protected void fillPreviousUrl(final PaginationBean bean, final PagedResult<?> searchResult, final Pagination pagination) {
        if (!searchResult.isFirst()) {
            bean.setPreviousUrl(buildUrlWithPage(pagination.getKey(), pagination.getPage() - 1));
        }
    }

    protected void fillPages(final PaginationBean bean, final PagedResult<?> searchResult, final Pagination pagination, final int pageSize) {
        final int displayedPages = configuration.getInt("pop.pagination.displayedPages", 6);
        final long totalPages = calculateTotalPages(searchResult.getTotal(), pageSize);
        final long thresholdLeft = displayedPages - 1;
        final long thresholdRight = totalPages - displayedPages + 2;
        if (totalPages <= displayedPages) {
            fillWithAllPages(bean, totalPages, pagination);
        } else if (pagination.getPage() < thresholdLeft) {
            fillWithLastAndRestPages(bean, totalPages, thresholdLeft, pagination);
        } else if (pagination.getPage() > thresholdRight) {
            fillWithFirstAndRestPages(bean, totalPages, thresholdRight, pagination);
        } else {
            fillWithFirstLastAndMiddlePages(bean, totalPages, pagination);
        }
    }

    protected void fillWithAllPages(final PaginationBean bean, final long totalPages, final Pagination pagination) {
        bean.setPages(createPages(pagination, 1, totalPages));
    }

    protected void fillWithFirstAndRestPages(final PaginationBean bean, final long totalPages, final long thresholdRight, final Pagination pagination) {
        bean.setPages(createPages(pagination, thresholdRight, totalPages));
        bean.setFirstPage(createLinkData(pagination, 1));
    }

    protected void fillWithLastAndRestPages(final PaginationBean bean, final long totalPages, final long thresholdLeft, final Pagination pagination) {
        bean.setPages(createPages(pagination, 1, thresholdLeft));
        bean.setLastPage(createLinkData(pagination, totalPages));
    }

    protected void fillWithFirstLastAndMiddlePages(final PaginationBean bean, final long totalPages, final Pagination pagination) {
        bean.setPages(createPages(pagination, pagination.getPage() - 1, pagination.getPage() + 1));
        bean.setFirstPage(createLinkData(pagination, 1));
        bean.setLastPage(createLinkData(pagination, totalPages));
    }

    protected List<LinkBean> createPages(final Pagination pagination, final long startPage, final long endPage) {
        return LongStream.rangeClosed(startPage, endPage)
                .mapToObj(page -> createLinkData(pagination, page))
                .collect(toList());
    }

    protected LinkBean createLinkData(final Pagination pagination, final long page) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(String.valueOf(page));
        linkBean.setUrl(buildUrlWithPage(pagination.getKey(), page));
        if (page == pagination.getPage()) {
            linkBean.setSelected(true);
        }
        return linkBean;
    }

    protected String buildUrlWithPage(final String key, final long page) {
        return requestContext.buildUrl(key, singletonList(String.valueOf(page)));
    }

    protected static long calculateTotalPages(final float total, final int pageSize) {
        final Double totalPages = Math.ceil(total / pageSize);
        return totalPages.longValue();
    }
}
