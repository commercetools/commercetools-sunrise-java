package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormSettings;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import static java.util.stream.Collectors.toList;

public abstract class AbstractEntriesPerPageSelectorViewModelFactory extends SimpleViewModelFactory<EntriesPerPageSelectorViewModel, PagedResult<?>> {

    private final EntriesPerPageFormSettings entriesPerPageFormSettings;
    private final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory;

    protected AbstractEntriesPerPageSelectorViewModelFactory(final EntriesPerPageFormSettings entriesPerPageFormSettings,
                                                             final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory) {
        this.entriesPerPageFormSettings = entriesPerPageFormSettings;
        this.entriesPerPageFormSelectableOptionViewModelFactory = entriesPerPageFormSelectableOptionViewModelFactory;
    }

    protected final EntriesPerPageFormSettings getEntriesPerPageFormSettings() {
        return entriesPerPageFormSettings;
    }

    protected final EntriesPerPageFormSelectableOptionViewModelFactory getEntriesPerPageFormSelectableOptionViewModelFactory() {
        return entriesPerPageFormSelectableOptionViewModelFactory;
    }

    @Override
    protected EntriesPerPageSelectorViewModel newViewModelInstance(final PagedResult<?> pagedResult) {
        return new EntriesPerPageSelectorViewModel();
    }

    @Override
    public final EntriesPerPageSelectorViewModel create(final PagedResult<?> pagedResult) {
        return super.create(pagedResult);
    }

    @Override
    protected final void initialize(final EntriesPerPageSelectorViewModel viewModel, final PagedResult<?> pagedResult) {
        fillKey(viewModel, pagedResult);
        fillList(viewModel, pagedResult);
    }

    protected void fillKey(final EntriesPerPageSelectorViewModel viewModel, final PagedResult<?> pagedResult) {
        viewModel.setKey(entriesPerPageFormSettings.getFieldName());
    }

    protected void fillList(final EntriesPerPageSelectorViewModel viewModel, final PagedResult<?> pagedResult) {
        final String selectedOptionValue = entriesPerPageFormSettings.getSelectedOption(Http.Context.current())
                .map(FormOption::getFieldValue)
                .orElse(null);
        viewModel.setList(entriesPerPageFormSettings.getOptions().stream()
                .map(option -> entriesPerPageFormSelectableOptionViewModelFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
