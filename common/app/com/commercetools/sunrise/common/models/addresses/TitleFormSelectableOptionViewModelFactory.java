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
    protected final void initialize(final TitleFormSelectableOptionViewModel model, final String option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final TitleFormSelectableOptionViewModel model, final String option, @Nullable final String selectedValue) {
        model.setLabel(option);
    }

    protected void fillValue(final TitleFormSelectableOptionViewModel model, final String option, @Nullable final String selectedValue) {
        model.setValue(option);
    }

    protected void fillSelected(final TitleFormSelectableOptionViewModel model, final String option, @Nullable final String selectedValue) {
        model.setSelected(option.equals(selectedValue));
    }
}
