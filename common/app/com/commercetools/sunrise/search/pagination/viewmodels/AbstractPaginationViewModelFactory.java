package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModelFactory;
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

public abstract class AbstractPaginationViewModelFactory extends ViewModelFactory {

    private final PaginationSettings paginationSettings;

    protected AbstractPaginationViewModelFactory(final PaginationSettings paginationSettings) {
        this.paginationSettings = paginationSettings;
    }

    protected final PaginationSettings getPaginationSettings() {
        return paginationSettings;
    }

    protected PaginationViewModel newViewModelInstance(final PagedResult<?> pagedResult, final long currentPage) {
        return new PaginationViewModel();
    }

    public final PaginationViewModel create(final PagedResult<?> pagedResult, final long currentPage) {
        final PaginationViewModel instance = newViewModelInstance(pagedResult, currentPage);
        initialize(instance, pagedResult, currentPage);
        return instance;
    }

    protected final void initialize(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        fillNextUrl(viewModel, pagedResult, currentPage);
        fillPreviousUrl(viewModel, pagedResult, currentPage);
        fillFirstPage(viewModel, pagedResult, currentPage);
        fillLastPage(viewModel, pagedResult, currentPage);
        fillPages(viewModel, pagedResult, currentPage);
    }

    protected void fillNextUrl(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        if (!pagedResult.isLast()) {
            viewModel.setNextUrl(buildUriWithPage(paginationSettings.getFieldName(), currentPage + 1));
        }
    }

    protected void fillPreviousUrl(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        if (!pagedResult.isFirst()) {
            viewModel.setPreviousUrl(buildUriWithPage(paginationSettings.getFieldName(), currentPage - 1));
        }
    }

    protected void fillFirstPage(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        if (firstPageIsDisplayed(currentPage)) {
            viewModel.setFirstPage(createLinkData(1, currentPage));
        }
    }

    protected void fillLastPage(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        final long totalPages = calculateTotalPages(pagedResult, currentPage);
        if (lastPageIsDisplayed(currentPage, totalPages)) {
            viewModel.setLastPage(createLinkData(totalPages, currentPage));
        }
    }

    protected void fillPages(final PaginationViewModel viewModel, final PagedResult<?> pagedResult, final long currentPage) {
        final long totalPages = calculateTotalPages(pagedResult, currentPage);
        long startPage = 1;
        long endPage = totalPages;
        if (firstPageIsDisplayed(currentPage)) {
            startPage = calculateBottomThresholdPage(currentPage);
        }
        if (lastPageIsDisplayed(currentPage, totalPages)) {
            endPage = calculateTopThresholdPage(currentPage, totalPages);
        }
        viewModel.setPages(createPages(startPage, endPage, currentPage));
    }

    private boolean firstPageIsDisplayed(final long currentPage) {
        return calculateBottomThresholdPage(currentPage) > 2;
    }

    private boolean lastPageIsDisplayed(final long currentPage, final long totalPages) {
        return calculateTopThresholdPage(currentPage, totalPages) < totalPages - 1;
    }

    private List<PaginationLinkViewModel> createPages(final long startPage, final long endPage, final long currentPage) {
        return LongStream.rangeClosed(startPage, endPage)
                .mapToObj(page -> createLinkData(page, currentPage))
                .collect(toList());
    }

    private PaginationLinkViewModel createLinkData(final long page, final long currentPage) {
        final PaginationLinkViewModel linkViewModel = new PaginationLinkViewModel();
        linkViewModel.setText(String.valueOf(page));
        linkViewModel.setUrl(buildUriWithPage(paginationSettings.getFieldName(), page));
        if (page == currentPage) {
            linkViewModel.setSelected(true);
        }
        return linkViewModel;
    }

    private String buildUriWithPage(final String key, final long page) {
        final Http.Request request = Http.Context.current().request();
        final Map<String, List<String>> queryString = extractQueryString(request);
        queryString.put(key, singletonList(String.valueOf(page)));
        return buildUri(request.path(), queryString);
    }

    private long calculateBottomThresholdPage(final long currentPage) {
        return Math.max(currentPage - paginationSettings.getDisplayedPages(), 1);
    }

    private long calculateTopThresholdPage(final long currentPage, final long totalPages) {
        return Math.min(currentPage + paginationSettings.getDisplayedPages(), totalPages);
    }

    private long calculateTotalPages(final PagedResult<?> pagedResult, final long currentPage) {
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
