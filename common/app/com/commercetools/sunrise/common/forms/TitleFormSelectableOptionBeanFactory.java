package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldFactory;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

@RequestScoped
public class TitleFormSelectableOptionBeanFactory extends FormFieldFactory<TitleFormSelectableOptionBean, TitleFormSelectableOptionBeanFactory.Data> {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public TitleFormSelectableOptionBeanFactory(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    public final TitleFormSelectableOptionBean create(final String titleKey, @Nullable final String selectedTitleKey) {
        final Data data = new Data(titleKey, selectedTitleKey);
        return initializedViewModel(data);
    }

    @Override
    protected TitleFormSelectableOptionBean getViewModelInstance() {
        return new TitleFormSelectableOptionBean();
    }

    @Override
    protected final void initialize(final TitleFormSelectableOptionBean bean, final Data data) {
        fillLabel(bean, data);
        fillValue(bean, data);
        fillSelected(bean, data);
    }

    protected void fillLabel(final TitleFormSelectableOptionBean bean, final Data data) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(data.titleKey);
        final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
        bean.setLabel(title);
    }

    protected void fillValue(final TitleFormSelectableOptionBean bean, final Data data) {
        bean.setValue(data.titleKey);
    }

    protected void fillSelected(final TitleFormSelectableOptionBean bean, final Data data) {
        bean.setSelected(data.titleKey.equals(data.selectedTitleKey));
    }

    protected final static class Data extends Base {

        public final String titleKey;
        @Nullable
        public final String selectedTitleKey;

        public Data(final String titleKey, final String selectedTitleKey) {
            this.titleKey = titleKey;
            this.selectedTitleKey = selectedTitleKey;
        }
    }
}
