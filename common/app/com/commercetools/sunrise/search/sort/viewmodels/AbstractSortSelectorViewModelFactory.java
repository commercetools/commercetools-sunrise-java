package com.commercetools.sunrise.search.sort.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;
import com.commercetools.sunrise.search.sort.SortFormSettings;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import static java.util.stream.Collectors.toList;

public abstract class AbstractSortSelectorViewModelFactory<T> extends SimpleViewModelFactory<SortSelectorViewModel, PagedResult<T>> {

    private final SortFormSettings<T> sortFormSettings;
    private final SortFormSelectableOptionViewModelFactory sortFormSelectableOptionViewModelFactory;

    protected AbstractSortSelectorViewModelFactory(final SortFormSettings<T> sortFormSettings,
                                                   final SortFormSelectableOptionViewModelFactory sortFormSelectableOptionViewModelFactory) {
        this.sortFormSettings = sortFormSettings;
        this.sortFormSelectableOptionViewModelFactory = sortFormSelectableOptionViewModelFactory;
    }

    protected final SortFormSettings getSortFormSettings() {
        return sortFormSettings;
    }

    protected final SortFormSelectableOptionViewModelFactory getSortFormSelectableOptionViewModelFactory() {
        return sortFormSelectableOptionViewModelFactory;
    }

    @Override
    protected SortSelectorViewModel newViewModelInstance(final PagedResult<T> pagedResult) {
        return new SortSelectorViewModel();
    }

    @Override
    public final SortSelectorViewModel create(final PagedResult<T> pagedResult) {
        return super.create(pagedResult);
    }

    @Override
    protected final void initialize(final SortSelectorViewModel viewModel, final PagedResult<T> pagedResult) {
        fillKey(viewModel, pagedResult);
        fillList(viewModel, pagedResult);
    }

    protected void fillKey(final SortSelectorViewModel viewModel, final PagedResult<T> pagedResult) {
        viewModel.setKey(sortFormSettings.getFieldName());
    }

    protected void fillList(final SortSelectorViewModel viewModel, final PagedResult<T> pagedResult) {
        final String selectedOptionValue = sortFormSettings.getSelectedOption(Http.Context.current())
                .map(FormOption::getFieldValue)
                .orElse(null);
        viewModel.setList(sortFormSettings.getOptions().stream()
                .map(option -> sortFormSelectableOptionViewModelFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
