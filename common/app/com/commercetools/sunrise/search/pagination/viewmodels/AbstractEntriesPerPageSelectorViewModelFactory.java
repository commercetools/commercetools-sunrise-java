package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormSettings;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import javax.annotation.Nullable;

import static java.util.stream.Collectors.toList;

public abstract class AbstractEntriesPerPageSelectorViewModelFactory extends SimpleViewModelFactory<EntriesPerPageSelectorViewModel, PagedResult<?>> {

    @Nullable
    private final String selectedOptionValue;
    private final EntriesPerPageFormSettings settings;
    private final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory;

    protected AbstractEntriesPerPageSelectorViewModelFactory(final EntriesPerPageFormSettings settings,
                                                             final EntriesPerPageFormSelectableOptionViewModelFactory entriesPerPageFormSelectableOptionViewModelFactory,
                                                             final Http.Context httpContext) {
        this.selectedOptionValue = settings.getSelectedOption(httpContext)
                .map(FormOption::getFieldValue)
                .orElse(null);
        this.settings = settings;
        this.entriesPerPageFormSelectableOptionViewModelFactory = entriesPerPageFormSelectableOptionViewModelFactory;
    }

    @Nullable
    protected final String getSelectedOptionValue() {
        return selectedOptionValue;
    }

    protected final EntriesPerPageFormSettings getSettings() {
        return settings;
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
        viewModel.setKey(settings.getFieldName());
    }

    protected void fillList(final EntriesPerPageSelectorViewModel viewModel, final PagedResult<?> pagedResult) {
        viewModel.setList(settings.getOptions().stream()
                .map(option -> entriesPerPageFormSelectableOptionViewModelFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
