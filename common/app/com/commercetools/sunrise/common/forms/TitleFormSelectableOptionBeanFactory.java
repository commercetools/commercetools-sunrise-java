package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class TitleFormSelectableOptionBeanFactory extends SelectableViewModelFactory<TitleFormSelectableOptionBean, String, String> {

    @Inject
    public TitleFormSelectableOptionBeanFactory() {
    }

    @Override
    protected TitleFormSelectableOptionBean getViewModelInstance() {
        return new TitleFormSelectableOptionBean();
    }

    @Override
    public final TitleFormSelectableOptionBean create(final String option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        model.setLabel(option);
    }

    protected void fillValue(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        model.setValue(option);
    }

    protected void fillSelected(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        model.setSelected(option.equals(selectedValue));
    }
}
