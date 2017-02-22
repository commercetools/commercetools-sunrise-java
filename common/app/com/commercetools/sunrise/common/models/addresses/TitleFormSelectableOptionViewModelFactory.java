package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class TitleFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<TitleFormSelectableOptionViewModel, String, String> {

    @Inject
    public TitleFormSelectableOptionViewModelFactory() {
    }

    @Override
    protected TitleFormSelectableOptionViewModel getViewModelInstance() {
        return new TitleFormSelectableOptionViewModel();
    }

    @Override
    public final TitleFormSelectableOptionViewModel create(final String option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final TitleFormSelectableOptionViewModel viewModel, final String option, @Nullable final String selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final TitleFormSelectableOptionViewModel viewModel, final String option, @Nullable final String selectedValue) {
        viewModel.setLabel(option);
    }

    protected void fillValue(final TitleFormSelectableOptionViewModel viewModel, final String option, @Nullable final String selectedValue) {
        viewModel.setValue(option);
    }

    protected void fillSelected(final TitleFormSelectableOptionViewModel viewModel, final String option, @Nullable final String selectedValue) {
        viewModel.setSelected(option.equals(selectedValue));
    }
}
