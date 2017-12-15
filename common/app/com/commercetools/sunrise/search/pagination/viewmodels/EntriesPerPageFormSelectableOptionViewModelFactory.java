package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.forms.SelectableViewModelFactory;
import com.commercetools.sunrise.search.pagination.EntriesPerPageFormOption;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class EntriesPerPageFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<EntriesPerPageFormSelectableOptionViewModel, EntriesPerPageFormOption, String> {

    private final I18nResolver i18nResolver;

    @Inject
    public EntriesPerPageFormSelectableOptionViewModelFactory(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    @Override
    protected EntriesPerPageFormSelectableOptionViewModel newViewModelInstance(final EntriesPerPageFormOption option, @Nullable final String selectedValue) {
        return new EntriesPerPageFormSelectableOptionViewModel();
    }

    @Override
    public final EntriesPerPageFormSelectableOptionViewModel create(final EntriesPerPageFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final EntriesPerPageFormSelectableOptionViewModel viewModel, final EntriesPerPageFormOption option, @Nullable final String selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final EntriesPerPageFormSelectableOptionViewModel viewModel, final EntriesPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setLabel(i18nResolver.getOrKey(option.getFieldLabel()));
    }

    protected void fillValue(final EntriesPerPageFormSelectableOptionViewModel viewModel, final EntriesPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setValue(option.getFieldValue());
    }

    protected void fillSelected(final EntriesPerPageFormSelectableOptionViewModel viewModel, final EntriesPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
