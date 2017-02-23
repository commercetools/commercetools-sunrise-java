package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductsPerPageFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<ProductsPerPageFormSelectableOptionViewModel, ProductsPerPageFormOption, String> {

    private final I18nIdentifierResolver i18nIdentifierResolver;

    @Inject
    public ProductsPerPageFormSelectableOptionViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
    }

    protected final I18nIdentifierResolver getI18nIdentifierResolver() {
        return i18nIdentifierResolver;
    }

    @Override
    protected ProductsPerPageFormSelectableOptionViewModel newViewModelInstance(final ProductsPerPageFormOption option, @Nullable final String selectedValue) {
        return new ProductsPerPageFormSelectableOptionViewModel();
    }

    @Override
    public final ProductsPerPageFormSelectableOptionViewModel create(final ProductsPerPageFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductsPerPageFormSelectableOptionViewModel viewModel, final ProductsPerPageFormOption option, @Nullable final String selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final ProductsPerPageFormSelectableOptionViewModel viewModel, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setLabel(i18nIdentifierResolver.resolveOrKey(option.getFieldLabel()));
    }

    protected void fillValue(final ProductsPerPageFormSelectableOptionViewModel viewModel, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setValue(option.getFieldValue());
    }

    protected void fillSelected(final ProductsPerPageFormSelectableOptionViewModel viewModel, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        viewModel.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
