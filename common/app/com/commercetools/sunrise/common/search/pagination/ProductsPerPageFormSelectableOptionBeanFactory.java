package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.models.SelectableViewModelFactory;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class ProductsPerPageFormSelectableOptionBeanFactory extends SelectableViewModelFactory<ProductsPerPageFormSelectableOptionBean, ProductsPerPageFormOption, String> {

    @Override
    protected ProductsPerPageFormSelectableOptionBean getViewModelInstance() {
        return new ProductsPerPageFormSelectableOptionBean();
    }

    @Override
    public final ProductsPerPageFormSelectableOptionBean create(final ProductsPerPageFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductsPerPageFormSelectableOptionBean model, final ProductsPerPageFormOption option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final ProductsPerPageFormSelectableOptionBean model, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        model.setLabel(option.getFieldLabel());
    }

    protected void fillValue(final ProductsPerPageFormSelectableOptionBean model, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        model.setValue(option.getFieldValue());
    }

    protected void fillSelected(final ProductsPerPageFormSelectableOptionBean model, final ProductsPerPageFormOption option, @Nullable final String selectedOptionValue) {
        model.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
