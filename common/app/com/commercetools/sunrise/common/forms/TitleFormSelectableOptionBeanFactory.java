package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableOptionViewModelFactory;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

@RequestScoped
public class TitleFormSelectableOptionBeanFactory extends SelectableOptionViewModelFactory<TitleFormSelectableOptionBean, String> {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public TitleFormSelectableOptionBeanFactory(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
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
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(option);
        final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
        model.setLabel(title);
    }

    protected void fillValue(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        model.setValue(option);
    }

    protected void fillSelected(final TitleFormSelectableOptionBean model, final String option, @Nullable final String selectedValue) {
        model.setSelected(option.equals(selectedValue));
    }
}
