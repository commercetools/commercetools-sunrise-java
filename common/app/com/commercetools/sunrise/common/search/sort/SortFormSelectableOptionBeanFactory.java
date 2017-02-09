package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Locale;

@RequestScoped
public class SortFormSelectableOptionBeanFactory extends SelectableViewModelFactory<SortFormSelectableOptionBean, SortFormOption, String> {

    private final Locale locale;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final I18nResolver i18nResolver;

    @Inject
    public SortFormSelectableOptionBeanFactory(final Locale locale, final I18nIdentifierFactory i18nIdentifierFactory,
                                               final I18nResolver i18nResolver) {
        this.locale = locale;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.i18nResolver = i18nResolver;
    }

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
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(option.getFieldLabel());
        model.setLabel(i18nResolver.getOrKey(Collections.singletonList(locale), i18nIdentifier));
    }

    protected void fillValue(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setValue(option.getFieldValue());
    }

    protected void fillSelected(final SortFormSelectableOptionBean model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
