package com.commercetools.sunrise.search.sort.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.framework.viewmodels.forms.SelectableViewModelFactory;
import com.commercetools.sunrise.search.sort.SortFormOption;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class SortFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<SortFormSelectableOptionViewModel, SortFormOption, String> {

    private final I18nIdentifierResolver i18nIdentifierResolver;

    @Inject
    public SortFormSelectableOptionViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
    }

    protected final I18nIdentifierResolver getI18nIdentifierResolver() {
        return i18nIdentifierResolver;
    }

    @Override
    protected SortFormSelectableOptionViewModel newViewModelInstance(final SortFormOption option, @Nullable final String selectedValue) {
        return new SortFormSelectableOptionViewModel();
    }

    @Override
    public final SortFormSelectableOptionViewModel create(final SortFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final SortFormSelectableOptionViewModel viewModel, final SortFormOption option, @Nullable final String selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final SortFormSelectableOptionViewModel viewModel, final SortFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setLabel(i18nIdentifierResolver.resolveOrKey(option.getFieldLabel()));
    }

    protected void fillValue(final SortFormSelectableOptionViewModel viewModel, final SortFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setValue(option.getFieldValue());
    }

    protected void fillSelected(final SortFormSelectableOptionViewModel viewModel, final SortFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
