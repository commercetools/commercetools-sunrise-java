package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.search.pagination.PaginationSettings;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.buildUri;
import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.extractQueryString;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public abstract class AbstractPaginationViewModelFactory extends SimpleViewModelFactory<PaginationViewModel, PagedResult<?>> {

    private final PaginationSettings settings;
    private final int currentPage;
    private final Http.Context httpContext;

    protected AbstractPaginationViewModelFactory(final PaginationSettings settings, final Http.Context httpContext) {
        this.settings = settings;
        this.currentPage = settings.getSelectedValue(httpContext);
        this.httpContext = httpContext;
    }

    protected final int getCurrentPage() {
        return currentPage;
    }

    protected final PaginationSettings getSettings() {
        return settings;
    }

    protected final Http.Context getHttpContext() {
        return httpContext;
    }

    @Override
    protected PaginationViewModel newViewModelInstance(final PagedResult<?> pagedResult) {
        return new PaginationViewModel();
    }

    @Override
    public final PaginationViewModel create(final PagedResult<?> pagedResult) {
        return super.create(pagedResult);
    }

    @Override
    protected final void initialize(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        fillNextUrl(viewModel, pagedResult);
        fillPreviousUrl(viewModel, pagedResult);
        fillFirstPage(viewModel, pagedResult);
        fillLastPage(viewModel, pagedResult);
        fillPages(viewModel, pagedResult);
    }

    protected void fillNextUrl(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        if (!pagedResult.isLast()) {
            viewModel.setNextUrl(buildUriWithPage(settings.getFieldName(), currentPage + 1));
        }
    }

    protected void fillPreviousUrl(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        if (!pagedResult.isFirst()) {
            viewModel.setPreviousUrl(buildUriWithPage(settings.getFieldName(), currentPage - 1));
        }
    }

    protected void fillFirstPage(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        if (firstPageIsDisplayed()) {
            viewModel.setFirstPage(createLinkData(1));
        }
    }

    protected void fillLastPage(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        final long totalPages = calculateTotalPages(pagedResult);
        if (lastPageIsDisplayed(totalPages)) {
            viewModel.setLastPage(createLinkData(totalPages));
        }
    }

    protected void fillPages(final PaginationViewModel viewModel, final PagedResult<?> pagedResult) {
        final long totalPages = calculateTotalPages(pagedResult);
        long startPage = 1;
        long endPage = totalPages;
        if (firstPageIsDisplayed()) {
            startPage = calculateBottomThresholdPage();
        }
        if (lastPageIsDisplayed(totalPages)) {
            endPage = calculateTopThresholdPage(totalPages);
        }
        viewModel.setPages(createPages(startPage, endPage));
    }

    private boolean firstPageIsDisplayed() {
        return calculateBottomThresholdPage() > 2;
    }

    private boolean lastPageIsDisplayed(final long totalPages) {
        return calculateTopThresholdPage(totalPages) < totalPages - 1;
    }

    private List<PaginationLinkViewModel> createPages(final long startPage, final long endPage) {
        return LongStream.rangeClosed(startPage, endPage)
                .mapToObj(this::createLinkData)
                .collect(toList());
    }

    private PaginationLinkViewModel createLinkData(final long page) {
        final PaginationLinkViewModel linkViewModel = new PaginationLinkViewModel();
        linkViewModel.setText(String.valueOf(page));
        linkViewModel.setUrl(buildUriWithPage(settings.getFieldName(), page));
        if (page == currentPage) {
            linkViewModel.setSelected(true);
        }
        return linkViewModel;
    }

    private String buildUriWithPage(final String key, final long page) {
        final Map<String, List<String>> queryString = extractQueryString(httpContext.request());
        queryString.put(key, singletonList(String.valueOf(page)));
        return buildUri(httpContext.request().path(), queryString);
    }

    private long calculateBottomThresholdPage() {
        return Math.max(currentPage - settings.getDisplayedPages(), 1);
    }

    private long calculateTopThresholdPage(final long totalPages) {
        return Math.min(currentPage + settings.getDisplayedPages(), totalPages);
    }

    private long calculateTotalPages(final PagedResult<?> pagedResult) {
        if (pagedResult.isLast()) {
            return currentPage;
        } else if (pagedResult.getCount() > 0) {
            final Double totalPages = Math.ceil(pagedResult.getTotal() / pagedResult.getCount());
            return totalPages.longValue();
        } else {
            return 0;
        }
    }
}
