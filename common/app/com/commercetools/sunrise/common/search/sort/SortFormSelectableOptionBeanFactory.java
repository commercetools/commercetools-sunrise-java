package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.common.models.SelectableViewModelFactory;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class SortFormSelectableOptionBeanFactory extends SelectableViewModelFactory<SortFormSelectableOptionBean, SortFormOption, String> {

    @Override
    protected SortFormSelectableOptionBean getViewModelInstance() {
        return new SortFormSelectableOptionBean();
    }

    @Override
    public final SortFormSelectableOptionBean create(final SortFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setLabel(option.getFieldLabel());
    }

    protected void fillValue(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setValue(option.getFieldValue());
    }

    protected void fillSelected(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
